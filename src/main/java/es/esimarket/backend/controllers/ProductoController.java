package es.esimarket.backend.controllers;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.repositories.ProductoRepository;
import es.esimarket.backend.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoService productoService;

    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> getProductos() {
        return ResponseEntity.ok(productoRepository.findAll());
    }

    @GetMapping("/productos/{tipo}")
    public ResponseEntity<List<Producto>> getProductos(@PathVariable("tipo") String tipo) {
        return ResponseEntity.ok(productoRepository.findByTipo(tipo));
    }

    @PostMapping("/productos")
    public ResponseEntity<String> createProducto(@RequestParam String v, @RequestParam int p,
                                                   @RequestParam String d, @RequestParam String n,
                                                   @RequestParam String t)
    {

        return productoService.nuevoProducto(v, p, d, n, t);
    }
}
