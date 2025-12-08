package es.esimarket.backend.repositories;
import es.esimarket.backend.dtos.MensajeDTO;
import es.esimarket.backend.entities.Mensaje;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.*;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, BigInteger> {
    List<MensajeDTO> findByIDChat(int chat, Sort sort);

}
