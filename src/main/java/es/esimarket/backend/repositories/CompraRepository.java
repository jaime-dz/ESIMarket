package es.esimarket.backend.repositories;
import es.esimarket.backend.entities.Compra;
import es.esimarket.backend.entities.id.CompraId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, CompraId> {
}
