package es.esimarket.backend.services;
import es.esimarket.backend.controllers.requests.CompraRequest;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.repositories.CompraRepository;
import es.esimarket.backend.entities.Compra;

import java.util.*;

@Service
public class CompraService {

    @Autowired
    public ProductoRepository productoRepository;

    @Autowired
    public CompraRepository compraRepository;

    @Autowired
    public VariosService variosService;

    public Boolean UsrPuedeHacerCompra(Usuario u, Producto p)
    {
        return u.getSaldoMoneda() > p.getPrecio();
    }

    public ResponseEntity<String> HacerCompra(String uDNI, CompraRequest request)
    {
        Producto p = productoRepository.findByID(request.idProd());

        if ( p.getuDNI_Vendedor().equals(uDNI) ){
            return ResponseEntity.ok("No puedes comprar tu propio producto ;)");
        }

        Compra c = new Compra(uDNI,request.idProd(),variosService.ObtenerFecha());

        compraRepository.save(c);

        return ResponseEntity.ok("Compra realizada correctamente");
    }

}