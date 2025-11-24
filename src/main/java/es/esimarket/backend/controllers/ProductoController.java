package es.esimarket.backend.controllers;
import es.esimarket.backend.dtos.ProductoDTO;
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
    public ResponseEntity<List<ProductoDTO>> getProductos() {

        List<Producto> productEntities = productoRepository.findAll();
        return ResponseEntity.ok(productoService.mostrar_productos(productEntities));
    }

    @GetMapping("/{tipo}")
    public ResponseEntity<List<ProductoDTO>> getProductos(@PathVariable("tipo") String tipo) {
        List<Producto> productEntities = productoRepository.findByTipo(tipo);
        return ResponseEntity.ok(productoService.mostrar_productos(productEntities));
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
