package es.esimarket.backend.repositories;
import es.esimarket.backend.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllExpiradoIsFalseOrRevocadoIsFalseByuser(String id);
    void deleteByToken(String token);
    Token findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE (t.expirado = true OR t.revocado = true) AND t.fechaExpiracion < :fechaLimite")
    void deleteTokensAntiguos(LocalDateTime fechaLimite);

    @Modifying
    @Transactional
    @Query("UPDATE Token t SET t.expirado = true WHERE t.fechaExpiracion < :ahora AND t.expirado = false")
    void actualizarTokensExpirados(LocalDateTime ahora);
}
