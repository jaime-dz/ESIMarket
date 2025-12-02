package es.esimarket.backend.services;
import java.util.ArrayList;
import java.util.List;
import es.esimarket.backend.repositories.PedidosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import es.esimarket.backend.dtos.PedidosDTO;
import es.esimarket.backend.entities.Compra;
import es.esimarket.backend.entities.Pedidos;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.mappers.PedidosMapper;

@Service
public class PedidosService{

    @Autowired
    private PedidosRepository pedidosRepository;

    @Autowired
    private PedidosMapper pedidosMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<PedidosDTO> mostrar_pedidos(){
        List<Pedidos> Pedidoss = pedidosRepository.findAll();
        List<PedidosDTO> PedidosDTOs = new ArrayList<>();

        for( Pedidos p : Pedidoss)
        {
            PedidosDTOs.add(pedidosMapper.toDto(p));
        }

        return PedidosDTOs;
    }

    public List<PedidosDTO> mostrar_pedidos_comprador(String uDNI)
    {
        List<Pedidos> pedidosNormales;
        List<PedidosDTO> pedidosDTO = new ArrayList<>();

        String sql = "Select * from Pedidos where IdCompra in(select iDCompra from Compra where IDProducto in (select ID from Producto where uDNIVendedor = ?))";

        pedidosNormales = jdbcTemplate.queryForList(sql,Pedidos.class,uDNI);

        for(Pedidos p: pedidosNormales)
        {
            pedidosDTO.add(pedidosMapper.toDto(p));
        }

        return pedidosDTO;

    }


}