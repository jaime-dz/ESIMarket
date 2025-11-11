package es.esimarket.backend.repositories;
import es.esimarket.backend.dtos.MensajeDTO;
import es.esimarket.backend.entities.Mensaje;
import es.esimarket.backend.entities.id.MensajeId;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, MensajeId> {
    List<MensajeDTO> findByid_IDChat(int chat, Sort sort);

}
