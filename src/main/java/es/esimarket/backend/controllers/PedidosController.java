package es.esimarket.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import es.esimarket.backend.dtos.PedidosDTO;

import es.esimarket.backend.repositories.PedidosRepository;
import es.esimarket.backend.services.PedidosService;

import java.util.*;

import es.esimarket.backend.services.JwtService;


@Controller
@RequestMapping("/pedidos")
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
    public ResponseEntity<List<PedidosDTO>> getPedidosVendedor(@CookieValue(name = "accessToken", required = false) String accessToken)
    {
        String uDNI = jwtservice.extraerDNI(accessToken);
        return ResponseEntity.ok(pedidosService.mostrar_pedidos_vendedor(uDNI));
    }

    @GetMapping("/bypurchaser")
    public ResponseEntity<List<PedidosDTO>> getPedidosComprador(@CookieValue(name = "accessToken", required = false) String accessToken)
    {
        String uDNI = jwtservice.extraerDNI(accessToken);
        return ResponseEntity.ok(pedidosService.mostrar_pedidos_comprador(uDNI));
    }

    @PatchMapping("/entregarpedido/{IdPedido}/{NTaquilla}")
    public ResponseEntity<String> patchPedidoVendedor(@CookieValue(name = "accessToken", required = false) String accessToken,@PathVariable(name = "IdPedido") int IdPedido,@PathVariable(name = "NTaquilla") int NTaquilla)
    {
        return ResponseEntity.ok(pedidosService.entregarPedido(IdPedido,NTaquilla));  //mirar si convviene mandarle el udni aunque no lo uses
    }

    @PatchMapping("/recogerpedido/{IdPedido}")
    public ResponseEntity<String> patchPedidoComprador(@CookieValue(name = "accessToken", required = false) String accessToken,@PathVariable(name = "IdPedido") int IdPedido)
    {
        return ResponseEntity.ok(pedidosService.recogerPedido(IdPedido));  //mirar si hay que enviar el udni aunque no lo uses
    }

    //El post de pedido va dentro de al funcion hacer compra

}
