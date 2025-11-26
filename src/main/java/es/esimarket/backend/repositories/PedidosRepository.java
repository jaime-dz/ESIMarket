package es.esimarket.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import es.esimarket.backend.entities.Pedidos;

public interface PedidosRepository extends JpaRepository<Pedidos, Integer>{
}