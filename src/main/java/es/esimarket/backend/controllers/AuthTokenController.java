package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.autenticacion.LoginRequest;
import es.esimarket.backend.controllers.autenticacion.RegisterRequest;
import es.esimarket.backend.controllers.autenticacion.TokenResponse;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthTokenController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthService authService;

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/signup")
    public String signupG() { return "signup"; }

    @GetMapping("/login")
    public String loginG() { return "login"; }

    @PostMapping("/signup")
    public ResponseEntity<TokenResponse> signup(@RequestBody final RegisterRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        final TokenResponse token = authService.registerUser(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody final LoginRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        final TokenResponse token = authService.loginUser(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader)
    {
        final TokenResponse token = authService.refreshToken(authHeader);
        return ResponseEntity.ok(token);
    }

}
