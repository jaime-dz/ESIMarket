package es.esimarket.backend.controllers;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.repositories.ProductoRepository;
import es.esimarket.backend.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import es.esimarket.backend.controllers.requests.ProductoRequest;

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

    @GetMapping("/patata")
    public ResponseEntity<List<Producto>> getProductosFiltroGenerico(@RequestParam String json)
    {
        return ResponseEntity.ok(productoService.FiltroProductosGenerico(json));
    }

    @PostMapping("/")
    public ResponseEntity<String> createProducto(@RequestBody final ProductoRequest request)
    
    //(@RequestParam String v, @RequestParam int p,
    //@RequestParam String d, @RequestParam String n,
    //@RequestParam String t, @RequestParam Producto.estado e,
    //@RequestParam Producto.PagoAceptado pa)
    {

        return productoService.nuevoProducto(request.uDNIVendedor(),request.precio(),request.descripcion(),request.Nombre(),request.Tipo(),request.estado(),request.pago(),request.recepcionAceptada());
    }
}
