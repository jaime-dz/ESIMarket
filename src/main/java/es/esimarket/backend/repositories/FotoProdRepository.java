package es.esimarket.backend.repositories;

import es.esimarket.backend.entities.FotoProd;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FotoProdRepository extends JpaRepository<FotoProd, Integer> {
    FotoProd findByIdProd( int id );
}
