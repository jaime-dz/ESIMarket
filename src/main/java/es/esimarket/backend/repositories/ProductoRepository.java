package es.esimarket.backend.repositories;
import es.esimarket.backend.entities.Compra;
import es.esimarket.backend.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByTipo(String Tipo);
    Producto findByid(int id);
}
