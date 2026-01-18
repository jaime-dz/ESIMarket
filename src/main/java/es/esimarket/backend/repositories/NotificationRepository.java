package es.esimarket.backend.repositories;
import es.esimarket.backend.entities.Notificacion;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notificacion, BigInteger> {
    void deleteById(@Nonnull BigInteger id);

    @Transactional
    void deleteByuDNI( String dni );

    boolean existsByuDNIAndMensaje(String uDNI, String mensaje);

    List<Notificacion> findByuDNIOrderByIdDesc(String uDNI);

    List<Notificacion> findByuDNI( String id );
}
