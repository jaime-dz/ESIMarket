package es.esimarket.backend.repositories;
import es.esimarket.backend.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllValidoIsFalseOrRevocadoIsFalseByuser_id(String id);
    Token findByToken(String token);
}
