package es.esimarket.backend.repositories;
import es.esimarket.backend.entities.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiciosRepository extends JpaRepository<Servicio, Integer> {
    Servicio findByidProd(int id);
}
