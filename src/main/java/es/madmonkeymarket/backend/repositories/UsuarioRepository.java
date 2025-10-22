package es.madmonkeymarket.backend.repositories;
import es.madmonkeymarket.backend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,String> {
}
