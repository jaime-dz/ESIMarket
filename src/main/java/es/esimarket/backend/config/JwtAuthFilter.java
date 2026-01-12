package es.esimarket.backend.config;
import es.esimarket.backend.entities.Token;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.repositories.TokenRepository;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull jakarta.servlet.http.HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // Obtener cookies de manera segura (maneja nulos)
        String accessToken = getCookieValue(request, "accessToken");
        String refreshToken = getCookieValue(request, "refreshToken");
        String isLoggedIn = getCookieValue(request, "isLoggedIn");

        // ---------------------------------------------------------
        // CASO 1: Usuario sin credenciales (Cookies vacías)
        // ---------------------------------------------------------
        if (accessToken == null && refreshToken == null) {
            limpiarCookies(response);
            if (esRutaPublica(path)) {
                filterChain.doFilter(request, response);
                return;
            } else {
                redirigirAlLogin(response); // Redirige si intenta entrar a perfil
            }
            return;
        }

        // ---------------------------------------------------------
        // CASO 2: Usuario con credenciales (Validamos Token)
        // ---------------------------------------------------------
        try {
            if (accessToken != null) {
                if (jwtService.isTokenExpired(accessToken)) {
                    throw new ExpiredJwtException(null, null, "Token expirado");
                }
                // Si el token es válido, autenticamos en el contexto
                String userDNI = jwtService.extraerDNI(accessToken);
                if (userDNI != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    autenticarUsuario(userDNI, request);
                }
            } else {
                // Si no hay accessToken pero sí refreshToken, forzamos error para intentar refresco
                throw new ExpiredJwtException(null, null, "AccessToken falta, probar Refresh");
            }

        } catch (ExpiredJwtException e) {
            // El AccessToken caducó, intentamos usar el RefreshToken
            if (refreshToken != null && attemptSilentRefresh(refreshToken, request, response)) {
                // Si el refresh funcionó, continuamos
                filterChain.doFilter(request, response);
            } else {
                // Si el refresh también falló -> Tratamos como token inválido
                gestionarTokenInvalido(request, response, filterChain);
            }

        } catch (Exception e) {
            // Cualquier otro error (firma mal, token corrupto, etc.)
            gestionarTokenInvalido(request, response, filterChain);
        }

        filterChain.doFilter(request, response);
    }


    private void gestionarTokenInvalido(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException, ServletException {
        // 1. Limpiamos la seguridad por si acaso
        SecurityContextHolder.clearContext();

        // 2. IMPORTANTE: Borramos las cookies malas del navegador
        limpiarCookies(response);

        // 3. Decisión de Redirección
        String path = request.getServletPath();
        if (esRutaPublica(path)) {
            // Si ya está intentando ir al Login o Home, LE DEJAMOS PASAR como anónimo.
            // NO redirigimos, porque eso causaría el bucle.
            filterChain.doFilter(request, response);
        } else {
            // Solo redirigimos si quería entrar a una zona privada (ej. /profile)
            redirigirAlLogin(response);
        }
    }

    private void limpiarCookies(jakarta.servlet.http.HttpServletResponse response) {
        ResponseCookie deleteAccess = ResponseCookie.from("accessToken", "")
                .path("/")
                .maxAge(0) // Caduca inmediatamente
                .build();
        ResponseCookie deleteRefresh = ResponseCookie.from("refreshToken", "")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie deleteLoggedIn = ResponseCookie.from("isLoggedIn", "")
                .path("/")
                .maxAge(0)
                .httpOnly(false) // Esta no suele ser HttpOnly
                .secure(false)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteRefresh.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteLoggedIn.toString());
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

                ResponseCookie isLoggedIn = ResponseCookie.from("isLoggedIn", "true")
                        .path("/") // Asegúrate que coincida con tu config original
                        .maxAge(jwtExpiration / 1000) // Convertir ms a segundos
                        .httpOnly(false)
                        .secure(false) // Pon true en producción (HTTPS)
                        .build();

                // Inyectar la cookie en la respuesta
                response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
                response.addHeader(HttpHeaders.SET_COOKIE,isLoggedIn.toString());

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

    private boolean esRutaPublica(String path) {
        return path.equals("/")
                || path.startsWith("/auth")
                || path.startsWith("/home")
                || path.equals("/error")
                || path.equals("/products/")
                || path.equals("/products/filter")
                || path.startsWith("/css")
                || path.startsWith("/js")
                || path.startsWith("/Images")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".ico")
                ||  path.endsWith(".pdf");


    }


}
