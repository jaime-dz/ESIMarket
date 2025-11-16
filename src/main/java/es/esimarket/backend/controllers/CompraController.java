package es.esimarket.backend.controllers;
import es.esimarket.backend.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import es.esimarket.backend.services.CompraService;

import java.util.List;

import es.esimarket.backend.repositories.CompraRepository;
import es.esimarket.backend.entities.Compra;

@Controller
@RequestMapping("/compras")
public class CompraController
{
    @Autowired
    private CompraRepository compraRepository;

    @Autowired

    private JwtService jwtService;

    @Autowired
    private CompraService compraService;

    @GetMapping("/")
    public ResponseEntity<List<Compra>> getComprasUsuario(HttpServletRequest request){

        String token = request.getHeader("Authorization").substring(7);
        String dni = jwtService.extraerDNI(token);

        return ResponseEntity.ok(compraRepository.findByid_uDNIComprador(dni));
    }

    @PostMapping("/")
    public ResponseEntity<String> postCompra(HttpServletRequest request, @RequestParam int idp)
    {
        String token = request.getHeader("Authorization").substring(7);
        String dni = jwtService.extraerDNI(token);

        return compraService.HacerCompra(dni, idp);
    }
}