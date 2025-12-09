package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.requests.CompraRequest;
import es.esimarket.backend.exceptions.CannotCompletePurchaseError;
import es.esimarket.backend.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import es.esimarket.backend.services.CompraService;

import java.util.HashMap;
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
    public ResponseEntity<List<Compra>> getComprasUsuario(@CookieValue(name = "accessToken", required = false) String accessToken){

        String dni = jwtService.extraerDNI(accessToken);
        return ResponseEntity.ok(compraRepository.findByuDNIComprador(dni));
    }

    @PostMapping("/")
    public ResponseEntity<HashMap<String,String>> postCompra(@CookieValue(name = "accessToken", required = false) String accessToken, @RequestBody final CompraRequest Crequest)
    {
        String dni = jwtService.extraerDNI(accessToken);

        HashMap<String,String> response = new HashMap<>();
        String respuestaService;
        try {
            respuestaService = compraService.HacerCompra(dni, Crequest);
        }catch (CannotCompletePurchaseError e){
            response.put("error", e.getMessage() );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("message",respuestaService);

        return ResponseEntity.ok(response);

    }
}