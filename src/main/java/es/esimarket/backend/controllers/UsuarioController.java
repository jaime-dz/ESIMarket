package es.esimarket.backend.controllers;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    public String signupG(@RequestBody Usuario user) {
        return usuarioService.registerUser(user); // Falta probarlo
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
