package es.esimarket.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import es.esimarket.backend.entities.Pedidos;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidosRepository extends JpaRepository<Pedidos, Integer>{

    @Query(value = "SELECT * FROM pedido WHERE Estado = 'PorEntregar' AND IdCompra IN (SELECT IdCompra FROM compra WHERE IDProducto IN (SELECT ID FROM producto WHERE uDNIVendedor = :uDNI))", nativeQuery = true)
    List<Pedidos> findPedidosByVendedor(@Param("uDNI") String uDNI);

    @Query(value = "SELECT p.* FROM pedido p JOIN compra c ON p.IdCompra = c.IdCompra /*WHERE p.Estado = 'Entregado'*/ AND c.uDNIComprador = :uDNI ORDER BY p.Estado DESC ", nativeQuery = true)
    List<Pedidos> findPedidosByComprador(@Param("uDNI") String uDNI);

}