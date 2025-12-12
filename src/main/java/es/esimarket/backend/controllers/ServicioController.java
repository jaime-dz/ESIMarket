package es.esimarket.backend.controllers;
import java.time.LocalDateTime;
import java.util.List;

import es.esimarket.backend.dtos.ProductoDTO;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.exceptions.CannotCompleteActionError;
import es.esimarket.backend.repositories.ProductoRepository;
import es.esimarket.backend.services.JwtService;
import es.esimarket.backend.services.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import es.esimarket.backend.controllers.requests.ProductoRequest;
import es.esimarket.backend.entities.Servicio;
import es.esimarket.backend.repositories.ServicioRepository;
import es.esimarket.backend.services.ServicioService;

@Controller
@RequestMapping("/service")
public class ServicioController{

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ServicioService servicioService;

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
    public ResponseEntity<List<Servicio>> GetServiciosUsuario(String DNIcomprador)
    {
        return ResponseEntity.ok(servicioService.mostrar_servicios_usuario(DNIcomprador));
    }
}