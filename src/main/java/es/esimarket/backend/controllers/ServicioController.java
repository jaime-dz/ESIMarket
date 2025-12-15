package es.esimarket.backend.controllers;

import es.esimarket.backend.entities.Servicio;
import es.esimarket.backend.repositories.ServicioRepository;
import es.esimarket.backend.services.JwtService;
import es.esimarket.backend.services.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/service")
public class ServicioController{

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private JwtService jwtService;

    @PatchMapping("/setdate")
    public ResponseEntity<String> ModificarFecha(int idProd,String DNIcomprador,LocalDateTime fecha)
    {
        return ResponseEntity.ok(servicioService.modificarFecha(idProd,DNIcomprador,fecha));
    }

    @PatchMapping("/end")
    public ResponseEntity<String> FinalizarServicio(int idProd, String DNIcomprador)
    {
        return ResponseEntity.ok(servicioService.finalizarServicio(idProd,DNIcomprador));
    }

    @GetMapping("/user")
    public ResponseEntity<List<Servicio>> GetServiciosUsuario(@CookieValue(name = "accessToken", required = false) String token)
    {
        String dni = jwtService.extraerDNI(token);
        return ResponseEntity.ok(servicioService.mostrar_servicios_usuario(dni));
    }
}