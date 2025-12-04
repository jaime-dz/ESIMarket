package es.esimarket.backend.repositories;
import es.esimarket.backend.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllExpiradoIsFalseOrRevocadoIsFalseByuser(String id);
    void deleteByExpiradoTrueOrRevocadoTrue();
    Token findByToken(String token);
}
