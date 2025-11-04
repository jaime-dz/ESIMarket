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

import es.esimarket.backend.services.CompraService;

import java.util.List;

import es.esimarket.backend.repositories.CompraRepository;
import es.esimarket.backend.entities.Compra;

@Controller
public class CompraController
{
    @Autowired
    public CompraRepository compraRepository;

    @Autowired
    public CompraService compraService;

    @GetMapping("/compras")
    public ResponseEntity<List<Compra>> getComprasUsuario(@RequestParam String u){
        return ResponseEntity.ok(compraRepository.findByid_uDNIComprador(u));
    }

    @PostMapping("/compras")
    public ResponseEntity<String> postCompra(@RequestParam String u, @RequestParam int idp)
    {
        return compraService.HacerCompra(u, idp);
    }
}