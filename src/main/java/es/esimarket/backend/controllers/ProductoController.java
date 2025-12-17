package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.requests.FiltroRequest;
import es.esimarket.backend.dtos.ProductoDTO;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.exceptions.CannotCompleteActionError;
import es.esimarket.backend.repositories.ProductoRepository;
import es.esimarket.backend.services.JwtService;
import es.esimarket.backend.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/")
    public ResponseEntity<List<ProductoDTO>> getProductos() {

        List<Producto> productEntities = productoRepository.findAll();
        return ResponseEntity.ok(productoService.mostrar_productos(productEntities));
    }

    @PostMapping("/filter")
    public ResponseEntity<List<ProductoDTO>> getProductosFiltroGenerico(@RequestBody FiltroRequest request)
    {
        List<Producto> productEntities = productoService.FiltroProductosGenerico(request);
        return ResponseEntity.ok(productoService.mostrar_productos(productEntities));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProducto( @RequestBody final ProductoRequest request)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();

        return productoService.nuevoProducto(dni,request.precio(),request.descripcion(),request.nombre(),request.tipo(),request.estado(),request.pago(),request.recepcionAceptada(),request.foto());
    }

    @DeleteMapping("/delete/{idProd}")
    public void  deleteProducto( @PathVariable int idProd) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();

        UserDetails userDetails = userDetailsService.loadUserByUsername(dni);

        Producto p = productoRepository.findByID(idProd);

        if ( !p.getuDNI_Vendedor().equals(dni) && userDetails.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new CannotCompleteActionError("No tienes permiso para hacer esta accion");
        }

        productoRepository.delete(p);

    }
}
