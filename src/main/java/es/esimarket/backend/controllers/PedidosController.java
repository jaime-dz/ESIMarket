package es.esimarket.backend.controllers;

import es.esimarket.backend.controllers.requests.TaquillaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import es.esimarket.backend.dtos.PedidosDTO;

import es.esimarket.backend.repositories.PedidosRepository;
import es.esimarket.backend.services.PedidosService;

import java.util.*;

import es.esimarket.backend.services.JwtService;


@Controller
@RequestMapping("/orders")
public class PedidosController{

    @Autowired
    private PedidosService pedidosService;

    @Autowired
    private JwtService jwtservice;

    @GetMapping("/all")
    public ResponseEntity<List<PedidosDTO>> getPedidos()
    {
        return ResponseEntity.ok(pedidosService.mostrar_pedidos());
    }

    @GetMapping("/byseller")
    public ResponseEntity<List<PedidosDTO>> getPedidosVendedor()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uDNI = auth.getName();
        return ResponseEntity.ok(pedidosService.mostrar_pedidos_vendedor(uDNI));
    }

    @GetMapping("/bypurchaser")
    public ResponseEntity<List<PedidosDTO>> getPedidosComprador()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uDNI = auth.getName();
        return ResponseEntity.ok(pedidosService.mostrar_pedidos_comprador(uDNI));
    }

    @PutMapping("/deliver/{IdPedido}/{NTaquilla}")
    public ResponseEntity<String> patchPedidoVendedor( @PathVariable(name = "IdPedido") int IdPedido,@PathVariable(name = "NTaquilla") int NTaquilla)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();

        return ResponseEntity.ok(pedidosService.entregarPedido(IdPedido,NTaquilla,dni));  //mirar si convviene mandarle el udni aunque no lo uses
    }

    @PutMapping("/pickup/{IdPedido}")
    public ResponseEntity<String> patchPedidoComprador( @PathVariable(name = "IdPedido") int IdPedido)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        return ResponseEntity.ok(pedidosService.recogerPedido(IdPedido,dni));
    }

}
