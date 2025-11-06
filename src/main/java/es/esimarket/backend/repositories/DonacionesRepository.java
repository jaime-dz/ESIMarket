package es.esimarket.backend.repositories;
import es.esimarket.backend.entities.Donaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonacionesRepository extends JpaRepository<Donaciones, String> {
}
