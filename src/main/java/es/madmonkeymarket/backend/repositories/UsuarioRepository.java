package es.madmonkeymarket.backend.repositories;
import es.madmonkeymarket.backend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,String> {
    boolean existsById(String id);
}
