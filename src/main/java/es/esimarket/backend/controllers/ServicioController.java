package es.esimarket.backend.controllers;

import es.esimarket.backend.dtos.ServicioDTO;
import es.esimarket.backend.repositories.ServicioRepository;
import es.esimarket.backend.services.JwtService;
import es.esimarket.backend.services.ServicioService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/setdate/{idProd}")
    public ResponseEntity<String> ModificarFecha(@PathVariable(name="idProd") Integer idProd, @RequestBody String fecha)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uDNI = auth.getName();
        return ResponseEntity.ok(servicioService.modificarFecha(idProd,uDNI,fecha));
    }

    @PatchMapping("/end/{idProd}")
    public ResponseEntity<String> FinalizarServicio(@PathVariable(name = "idProd") Integer idProd)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uDNI = auth.getName();
        return ResponseEntity.ok(servicioService.finalizarServicio(idProd,uDNI));
    }

    @GetMapping("/")
    public String getServicios(){
        return "service-list";
    }

    @PostMapping("/user")
    @ResponseBody
    public ResponseEntity<List<ServicioDTO>> GetServiciosUsuario()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        return ResponseEntity.ok(servicioService.mostrar_servicios_usuario(dni));
    }
}