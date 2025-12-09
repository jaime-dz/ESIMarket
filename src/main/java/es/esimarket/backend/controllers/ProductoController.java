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

    @GetMapping("/filter")
    public ResponseEntity<List<ProductoDTO>> getProductosFiltroGenerico(@RequestParam String json)
    {
        List<Producto> productEntities = productoService.FiltroProductosGenerico(json);
        return ResponseEntity.ok(productoService.mostrar_productos(productEntities));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProducto(@RequestBody final ProductoRequest request)
    {

        return productoService.nuevoProducto(request.uDNIVendedor(),request.precio(),request.descripcion(),request.Nombre(),request.Tipo(),request.estado(),request.pago(),request.recepcionAceptada(),request.foto());
    }
}
