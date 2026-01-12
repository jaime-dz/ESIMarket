package es.esimarket.backend.repositories;
import es.esimarket.backend.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Integer> {
    Chat findByid(Integer id);
    List<Chat> findByUDNIcompradorOrUDNIvendedor(String uDNI1, String uDNI2);
}
