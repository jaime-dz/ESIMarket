package es.esimarket.backend.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
    public ResponseEntity<String> signupG(@RequestParam String username,@RequestParam String password) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeySpecException {
        return usuarioService.registerUser(username,password); // Falta probarlo
    }

    @GetMapping("/signup")
    public String signupP() { return "signup.html"; } // NO VA ( Cosas del html que no reconoce ns por que ;) )


    @PostMapping("/login")
    public String loginP(@ModelAttribute Usuario user) {
        return ""; // Falta
    }

    @GetMapping("/login")
    public String loginG() { return "login.html"; }


}
