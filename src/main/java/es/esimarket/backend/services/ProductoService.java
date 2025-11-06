package es.esimarket.backend.services;
import es.esimarket.backend.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.esimarket.backend.entities.Producto;

@Service
public class ProductoService {

    @Autowired
    public ProductoRepository productoRepository;

    public ResponseEntity<String> nuevoProducto(String vendedor, int precio, String descripcion, String Nombre, String tipo){

        Producto p = new Producto(vendedor, precio, descripcion, Nombre, tipo);

        productoRepository.save(p);

        return ResponseEntity.ok("Producto registrado correctamente");
    }
}

