package es.esimarket.backend.config;
import es.esimarket.backend.entities.Token;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.repositories.TokenRepository;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
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

    @Override
    protected void doFilterInternal(@NonNull jakarta.servlet.http.HttpServletRequest request,
                                    @NonNull jakarta.servlet.http.HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {

        String path = request.getServletPath();

        if (path.equals("/")
		|| path.startsWith("/auth")
                || path.startsWith("/home")
                || path.startsWith("/usuarios")
                || path.startsWith("/compras")
                || path.startsWith("/mensajes")
                || path.startsWith("/css")
                || path.startsWith("/js")
                || path.startsWith("/Images")  ) {
            filterChain.doFilter( request, response);
            return;
        }

        String jwtToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String userDNI = jwtService.extraerDNI(jwtToken);

        if ( userDNI == null || SecurityContextHolder.getContext().getAuthentication() != null ) {
            filterChain.doFilter(request, response);
            return;
        }

        final Token token = tokenRepository.findByToken(jwtToken);
        if ( token == null || token.getExpirado() || token.getRevocado() ) {
            filterChain.doFilter(request,response);
            return;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userDNI);
        final Optional<Usuario> user = Optional.ofNullable(usuarioRepository.findByid(userDetails.getUsername()));
        if ( user.isEmpty() ) {
            filterChain.doFilter(request,response);
            return;
        }

        final boolean isTokenValid = jwtService.isTokenValid(jwtToken, user.get());
        if ( !isTokenValid ) {
            filterChain.doFilter(request, response);
            return;
        }

        final var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails((jakarta.servlet.http.HttpServletRequest) request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request,response);
    }

}
