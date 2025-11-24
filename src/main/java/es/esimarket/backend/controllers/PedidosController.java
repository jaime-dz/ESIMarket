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
    private PedidosRepository pedidosRepository;

    @Autowired
    private PedidosService pedidosService;

    @GetMapping("/")
    public ResponseEntity<List<PedidosDTO>> getPedidos()
    {
        return ResponseEntity.ok(pedidosService.mostrar_pedidos());
    }

    //El post de pedido va dentro de al funcion hacer compra

}
