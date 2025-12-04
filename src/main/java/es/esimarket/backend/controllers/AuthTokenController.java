package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.requests.LoginRequest;
import es.esimarket.backend.controllers.requests.RegisterRequest;
import es.esimarket.backend.controllers.autenticacion.TokenResponse;
import es.esimarket.backend.dtos.UsuarioDTO;
import es.esimarket.backend.exceptions.CannotCreateTokenError;
import es.esimarket.backend.exceptions.CannotCreateUserError;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthTokenController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    @Autowired
    private AuthService authService;

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        return ResponseEntity.ok(authService.mostrar_usuarios());
    }

    @GetMapping("/signup")
    public String signupG() { return "signup"; }

    @GetMapping("/login")
    public String loginG() { return "login"; }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody final RegisterRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException, CannotCreateUserError
    {
        final TokenResponse token = authService.registerUser(request);

        ResponseCookie jwtCookie = crearCookie("accessToken", token.accessToken(), jwtExpiration );
        ResponseCookie refreshCookie = crearCookie("refreshToken", token.refreshToken(), refreshExpiration);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody final LoginRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException, CannotCreateUserError
    {
        final TokenResponse token = authService.loginUser(request);

        ResponseCookie jwtCookie = crearCookie("accessToken", token.accessToken(), jwtExpiration);
        ResponseCookie refreshCookie = crearCookie("refreshToken", token.refreshToken(), refreshExpiration);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "refreshToken", required = false) String refreshToken, @CookieValue(name = "accessToken", required = false) String accessToken) throws  CannotCreateUserError, CannotCreateTokenError
    {

        authService.logout_user(refreshToken);

        ResponseCookie jwtCookie = crearCookie("accessToken", "", 0);
        ResponseCookie refreshCookie = crearCookie("refreshToken", "", 0);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();

    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) throws CannotCreateTokenError
    {
        final TokenResponse token = authService.refreshToken(refreshToken);

        ResponseCookie jwtCookie = crearCookie("accessToken", token.accessToken(), jwtExpiration);
        ResponseCookie refreshCookie = crearCookie("refreshToken", token.refreshToken(), refreshExpiration);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();

    }

    private ResponseCookie crearCookie(String nombre, String valor, long duracion) {
        return ResponseCookie.from(nombre, valor)
                .httpOnly(true) // Seguridad: JS no puede leerla
                .secure(false)  // false para localhost, true para producción (HTTPS)
                .path("/")
                .maxAge(duracion / 1000) // Segundos
                .sameSite("Strict")
                .build();

    }
}
