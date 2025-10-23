package es.madmonkeymarket.backend.controllers;
import es.madmonkeymarket.backend.entities.Usuario;
import es.madmonkeymarket.backend.repositories.UsuarioRepository;
import es.madmonkeymarket.backend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/usuarios")
@RestController
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute Usuario user) {
        return usuarioService.registerUser(user);
    }

    @GetMapping("/signup")
    public String about() { return "signup.html"; }




}
