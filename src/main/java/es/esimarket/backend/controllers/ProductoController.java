package es.esimarket.backend.controllers;
import es.esimarket.backend.dtos.ProductoDTO;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.exceptions.CannotCompleteActionError;
import es.esimarket.backend.repositories.ProductoRepository;
import es.esimarket.backend.services.JwtService;
import es.esimarket.backend.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import es.esimarket.backend.controllers.requests.ProductoRequest;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

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
    public ResponseEntity<String> createProducto(@CookieValue(name = "accessToken", required = false) String token,@RequestBody final ProductoRequest request)
    {
        String dni = jwtService.extraerDNI(token);

        return productoService.nuevoProducto(dni,request.precio(),request.descripcion(),request.Nombre(),request.Tipo(),request.estado(),request.pago(),request.recepcionAceptada(),request.foto());
    }

    @DeleteMapping("/delete/{idProd}")
    public void  deleteProducto(@CookieValue(name = "accessToken", required = false) String token, @PathVariable int idProd) {

        String dni = jwtService.extraerDNI(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(dni);

        Producto p = productoRepository.findByID(idProd);

        if ( !p.getuDNI_Vendedor().equals(dni) && userDetails.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new CannotCompleteActionError("No tienes permiso para hacer esta accion");
        }

        productoRepository.delete(p);

    }
}
