package es.esimarket.backend.services;
import es.esimarket.backend.controllers.requests.FiltroProdRequest;
import es.esimarket.backend.dtos.ProductoDTO;
import es.esimarket.backend.dtos.UsuarioDTO;
import es.esimarket.backend.entities.FotoProd;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.mappers.ProductMapper;
import es.esimarket.backend.repositories.FotoProdRepository;
import es.esimarket.backend.repositories.ProductoRepository;

import es.esimarket.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import es.esimarket.backend.entities.Producto;

import java.util.*;

import org.springframework.jdbc.core.JdbcTemplate;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private FotoProdRepository fotoProdRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private VariosService variosService;


    public ResponseEntity<String> nuevoProducto(String vendedor, int precio, String descripcion, String Nombre, String tipo, Producto.estado estado,Producto.PagoAceptado pa,Producto.RecepcionAceptada recepcionAceptada, byte[] foto ){

        Producto p = new Producto(vendedor, precio, descripcion, Nombre, tipo, estado,pa,recepcionAceptada);
        p = productoRepository.save(p);
        FotoProd fp = new FotoProd(p.getID(),foto);

        fotoProdRepository.save(fp);

        return ResponseEntity.ok("Producto registrado correctamente");
    }

    public List<ProductoDTO> mostrar_productos( List<Producto> productEntities ){

        List<ProductoDTO> productDTOs = new ArrayList<>();

        for( Producto p : productEntities){

            FotoProd fp = fotoProdRepository.findByIdProd(p.getID());
            Usuario u = usuarioRepository.findByid(p.getuDNI_Vendedor());
            productDTOs.add(productMapper.toDTO(p,fp,u));
        }

        return productDTOs;
    }
}

