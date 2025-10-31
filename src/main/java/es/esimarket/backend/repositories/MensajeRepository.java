package es.esimarket.backend.repositories;
import es.esimarket.backend.entities.Mensaje;
import es.esimarket.backend.entities.id.MensajeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, MensajeId> {
}
