package es.esimarket.backend.repositories;
import es.esimarket.backend.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Integer> {
    Chat findByID(Integer ID);
    List<Chat> findByuDNIcompradorOruDNIvendedor(String uDNI1, String uDNI2);
}
