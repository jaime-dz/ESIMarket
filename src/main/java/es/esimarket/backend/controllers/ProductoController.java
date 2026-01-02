package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.requests.FiltroProdRequest;
import es.esimarket.backend.dtos.ProductoDTO;
import es.esimarket.backend.entities.FotoProd;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.exceptions.CannotCompleteActionError;
import es.esimarket.backend.exceptions.CannotCreatePhotoError;
import es.esimarket.backend.exceptions.CannotCreateProductError;
import es.esimarket.backend.exceptions.CannotCreateUserError;
import es.esimarket.backend.mappers.ProductMapper;
import es.esimarket.backend.repositories.FotoProdRepository;
import es.esimarket.backend.repositories.ProductoRepository;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.JwtService;
import es.esimarket.backend.services.ProductoService;
import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private FotoProdRepository fotoProdRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/")
    public ResponseEntity<List<ProductoDTO>> getProductos() {

        List<Producto> productEntities = productoRepository.findAll();
        return ResponseEntity.ok(productoService.mostrar_productos(productEntities));
    }

    @GetMapping("/view/{ID}")
    public String viewProduct(Model model, @PathVariable(name="ID") int id ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();

        Producto p = productoRepository.findById(id).orElseThrow(()-> new CannotCreateProductError("Producto no encontrado"));
        FotoProd fp = fotoProdRepository.findById(id).orElse(null);
        Usuario u = usuarioRepository.findById(p.getuDNI_Vendedor()).orElseThrow(()-> new CannotCreateUserError("Usuario no encontrado"));
        ProductoDTO pDTO = new ProductoDTO(p,u,fp);

        boolean owner = dni.equals(p.getuDNI_Vendedor());

        model.addAttribute("product",pDTO);
        model.addAttribute("isOwner",owner);

        return "product-view";
    }

    @PostMapping("/filter")
    public ResponseEntity<List<ProductoDTO>> getProductosFiltroGenerico(@RequestBody FiltroProdRequest request)
    {
        List<Producto> productEntities = productoService.FiltroProductosGenerico(request);
        return ResponseEntity.ok(productoService.mostrar_productos(productEntities));
    }

    @GetMapping("/create")
    public String createProduct(){
        return "product-create";
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

        if ( !p.isDisponible() ) throw new CannotCreateProductError("No puedes borrar un produccto ya comprado");

        productoRepository.delete(p);

    }
}
