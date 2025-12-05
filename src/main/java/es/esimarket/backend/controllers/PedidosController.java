package es.esimarket.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import es.esimarket.backend.dtos.PedidosDTO;

import es.esimarket.backend.repositories.PedidosRepository;
import es.esimarket.backend.services.PedidosService;

import java.util.*;


@Controller
@RequestMapping("/pedidos")
public class PedidosController{

    @Autowired
    private PedidosService pedidosService;

    @GetMapping("/all")
    public ResponseEntity<List<PedidosDTO>> getPedidos()
    {
        return ResponseEntity.ok(pedidosService.mostrar_pedidos());
    }

    @GetMapping("/byseller")
    public ResponseEntity<List<PedidosDTO>> getPedidosVendedor(@CookieValue(name = "refreshToken", required = false) String refreshToken, @CookieValue(name = "accessToken", required = false) String accessToken, String uDNI)
    {
        return ResponseEntity.ok(pedidosService.mostrar_pedidos_vendedor(uDNI));
    }

    @GetMapping("/bypurchaser")
    public ResponseEntity<List<PedidosDTO>> getPedidosComprador(@CookieValue(name = "refreshToken", required = false) String refreshToken, @CookieValue(name = "accessToken", required = false) String accessToken, String uDNI)
    {
        return ResponseEntity.ok(pedidosService.mostrar_pedidos_comprador(uDNI));
    }

    @PatchMapping("/entregarpedido")
    public ResponseEntity<String> patchPedidoVendedor(@CookieValue(name = "refreshToken", required = false) String refreshToken, @CookieValue(name = "accessToken", required = false) String accessToken, int IdPedido,int NTaquilla)
    {
        return ResponseEntity.ok(pedidosService.entregarPedido(IdPedido,NTaquilla));  //mirar si convviene mandarle el udni aunque no lo uses
    }

    @PatchMapping("/recogerpedido")
    public ResponseEntity<String> patchPedidoComprador(@CookieValue(name = "refreshToken", required = false) String refreshToken, @CookieValue(name = "accessToken", required = false) String accessToken, int IdPedido)
    {
        return ResponseEntity.ok(pedidosService.recogerPedido(IdPedido));  //mirar si hay que enviar el udni aunque no lo uses
    }

    //El post de pedido va dentro de al funcion hacer compra

}
