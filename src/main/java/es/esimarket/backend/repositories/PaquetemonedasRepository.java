package es.esimarket.backend.repositories;
import es.esimarket.backend.entities.Paquetemonedas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaquetemonedasRepository extends JpaRepository<Paquetemonedas, Integer> {
}
