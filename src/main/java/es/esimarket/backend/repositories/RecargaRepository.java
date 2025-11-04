package es.esimarket.backend.repositories;
import es.esimarket.backend.entities.Recarga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecargaRepository extends JpaRepository<Recarga, Integer> {
}
