package es.madmonkeymarket.backend.controllers;
import es.madmonkeymarket.backend.entities.Usuario;
import es.madmonkeymarket.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> getUsers() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }
}
