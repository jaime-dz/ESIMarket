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
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoService productoService;

    @GetMapping("/")
    public ResponseEntity<List<Producto>> getProductos() {
        return ResponseEntity.ok(productoRepository.findAll());
    }

    @GetMapping("/{tipo}")
    public ResponseEntity<List<Producto>> getProductos(@PathVariable("tipo") String tipo) {
        return ResponseEntity.ok(productoRepository.findByTipo(tipo));
    }

    @PostMapping("/")
    public ResponseEntity<String> createProducto(@RequestParam String v, @RequestParam int p,
                                                   @RequestParam String d, @RequestParam String n,
                                                   @RequestParam String t, @RequestParam Producto.estado e)
    {

        return productoService.nuevoProducto(v, p, d, n, t, e);
    }
}
