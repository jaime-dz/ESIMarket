package es.esimarket.backend.services;
import java.util.ArrayList;
import java.util.List;

import es.esimarket.backend.controllers.requests.TaquillaRequest;
import es.esimarket.backend.exceptions.CannotCompleteActionError;
import es.esimarket.backend.repositories.CompraRepository;
import es.esimarket.backend.repositories.PedidosRepository;
import es.esimarket.backend.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import es.esimarket.backend.dtos.PedidosDTO;
import es.esimarket.backend.entities.Compra;
import es.esimarket.backend.entities.Pedidos;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.mappers.PedidosMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidosService{

    @Autowired
    private PedidosRepository pedidosRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ProductoRepository productoRepository;

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

    public List<PedidosDTO> mostrar_pedidos_vendedor(String uDNI)
    {
        List<Pedidos> pedidosNormales = pedidosRepository.findPedidosByVendedor(uDNI);
        List<PedidosDTO> pedidosDTO = new ArrayList<>();

        //String sql = "Select * from pedido where Estado = 'PorEntregar' and IdCompra in(select iDCompra from compra where IDProducto in (select ID from producto where uDNIVendedor = ?))";

        //pedidosNormales = jdbcTemplate.queryForList(sql,Pedidos.class,uDNI);

        for(Pedidos p: pedidosNormales)
        {
            pedidosDTO.add(pedidosMapper.toDto(p));
        }

        return pedidosDTO;

    }

    public List<PedidosDTO> mostrar_pedidos_comprador(String uDNI)
    {

        List<Pedidos> pedidosNormales = pedidosRepository.findPedidosByComprador(uDNI);
        List<PedidosDTO> pedidosDTO = new ArrayList<>();

        //String sql = "Select * from pedido where Estado = 'Entregado' and IdCompra in(select iDCompra from compra where uDNIComprador = ?)";

        //pedidosNormales = jdbcTemplate.queryForList(sql,Pedidos.class,uDNI);

        for(Pedidos p: pedidosNormales)
        {
            pedidosDTO.add(pedidosMapper.toDto(p));
        }

        return pedidosDTO;

    }

    public String entregarPedido(int IdPedido,int NTaquilla, String dni)
    {
        Pedidos ped = pedidosRepository.findById(IdPedido).orElseThrow(() -> new CannotCompleteActionError("Usuario no encontrado"));
        Compra c = compraRepository.findById(ped.getIdCompra()).orElseThrow( () -> new CannotCompleteActionError("Compra no encontrada") );
        Producto p = productoRepository.findById(c.getIDProducto()).orElseThrow( () -> new CannotCompleteActionError("Producto no encontrada"));
        if ( !p.getuDNI_Vendedor().equals(dni) ) throw new CannotCompleteActionError("Debes ser propietario del producto para entregarlo");
        ped.setEstado(Pedidos.Estado.Entregado);
        if ( c.getRecepcion() == Producto.RecepcionAceptada.enTaquilla ) ped.setNTaquilla(NTaquilla);

        pedidosRepository.save(ped);

        if ( c.getRecepcion() == Producto.RecepcionAceptada.enMano && c.getTipoPago() == Producto.PagoAceptado.Trueque ){
            Producto pT = productoRepository.findById(c.getIdProdTrueque()).orElseThrow( () -> new CannotCompleteActionError("Producto no encontrado") );
        }

        return "Se ha entregado su pedido con exito";
    }

    @Transactional
    public String recogerPedido(int IdPedido, String dni )
    {
        Pedidos p = pedidosRepository.findById(IdPedido).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Compra c = compraRepository.findById(p.getIdCompra()).orElseThrow( () -> new CannotCompleteActionError("Compra no encontrada") );
        Producto prod = productoRepository.findById(c.getIDProducto()).orElseThrow( () -> new CannotCompleteActionError("Producto no encontrado") );

        if ( !c.getuDNIComprador().equals(dni) ) throw new CannotCompleteActionError("Debes ser el comprador del producto para poder recogerlo");
        p.setEstado(Pedidos.Estado.Recogido);

        pedidosRepository.save(p);

        return "Se ha recogido el pedido con exito";
    }


}