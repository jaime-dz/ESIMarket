package es.esimarket.backend.config;
import es.esimarket.backend.entities.Token;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.repositories.TokenRepository;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    private static final String RUTA_LOGIN = "/auth/login";

    @Override
    protected void doFilterInternal(@NonNull jakarta.servlet.http.HttpServletRequest request,
                                    @NonNull jakarta.servlet.http.HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {

        String path = request.getServletPath();


        if (path.equals("/")
		        || path.startsWith("/auth")
                || path.startsWith("/home")
                || path.startsWith("/css")
                || path.startsWith("/js")
                || path.startsWith("/Images")  ) {
            filterChain.doFilter( request, response);
            return;
        }

        String accessToken = getCookieValue(request, "accessToken");
        String refreshToken = getCookieValue(request, "refreshToken");


        if (accessToken == null && refreshToken == null) {
            redirigirAlLogin(response);
            return;
        }

        try {
            if ( accessToken != null ){
                String userDNI = jwtService.extraerDNI(accessToken);

                if ( userDNI != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
                    autenticarUsuario(userDNI, request);
                }
            }else {
                throw new ExpiredJwtException(null, null, "Token nulo, forzar refresh");
            }

        }catch (ExpiredJwtException e){
            if ( refreshToken != null ) {
                boolean refreshExitoso = attemptSilentRefresh(refreshToken, request, response);

                if (!refreshExitoso) {
                    // Falló el refresh (expirado o revocado) -> Redirigir
                    redirigirAlLogin(response);
                    return;
                }
            }else{
                redirigirAlLogin(response);
                return;
            }

        }catch ( Exception e ){
            SecurityContextHolder.clearContext();
            redirigirAlLogin(response);
            return;
        }

        filterChain.doFilter(request,response);
    }

    private void redirigirAlLogin(jakarta.servlet.http.HttpServletResponse response) throws IOException {
        SecurityContextHolder.clearContext(); // Limpiamos por seguridad
        response.sendRedirect(RUTA_LOGIN);    // Ordenamos al navegador cambiar de página
    }

    private String getCookieValue(HttpServletRequest req, String name) {
        if (req.getCookies() == null) return null;
        return Arrays.stream(req.getCookies())
                .filter(c -> name.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private boolean attemptSilentRefresh(String refreshToken, HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) {
        try {
            // Extraer usuario del Refresh Token
            String userDNI = jwtService.extraerDNI(refreshToken);
            if (userDNI == null) return false;

            // Buscar el Refresh Token en la BD (CRÍTICO: debe existir y ser válido)
            Token tokenEnBD = tokenRepository.findByToken(refreshToken);

            // Validaciones de Seguridad
            if (tokenEnBD == null || tokenEnBD.getRevocado() || tokenEnBD.getExpirado()) {
                return false; // Token inválido o revocado, no renovamos
            }

            // Validar usuario
            Usuario usuario = usuarioRepository.findByid(userDNI);
            if (usuario == null) return false;

            //   Verificar firma criptográfica del Refresh Token
            if (jwtService.isTokenValid(refreshToken, usuario)) {

                // ¡ÉXITO! Generar NUEVO Access Token
                String newAccessToken = jwtService.generateToken(usuario);

                // Crear la Cookie con el nuevo token
                ResponseCookie jwtCookie = ResponseCookie.from("accessToken", newAccessToken)
                        .path("/") // Asegúrate que coincida con tu config original
                        .maxAge(jwtExpiration / 1000) // Convertir ms a segundos
                        .httpOnly(true)
                        .secure(false) // Pon true en producción (HTTPS)
                        .build();

                // Inyectar la cookie en la respuesta
                response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

                // Autenticar al usuario para que PASE este filtro
                autenticarUsuario(userDNI, request);
                return true;
            }
        } catch (Exception e) {
           return false;
        }
        return false;
    }

    private void autenticarUsuario(String userDNI, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userDNI);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

}
