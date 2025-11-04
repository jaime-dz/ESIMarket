package es.esimarket.backend.controllers;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signupG(@RequestParam String username,@RequestParam String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
        return usuarioService.registerUser(username,password);
    }

    @GetMapping("/signup")
    public String signupP() { return "signup.html"; }


    @PostMapping("/login")
    public ResponseEntity<String> loginP(@RequestParam String username, @RequestParam String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
        return usuarioService.loginUser(username,password);
    }

    @GetMapping("/login")
    public String loginG() { return "login.html"; }


}
