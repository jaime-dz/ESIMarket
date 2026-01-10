package es.esimarket.backend.repositories;
import es.esimarket.backend.entities.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {

    List<Compra> findByuDNIComprador(String id);
    Compra findByIDProducto( Integer id );
}
