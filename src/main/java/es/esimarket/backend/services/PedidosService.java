package es.esimarket.backend.services;
import java.util.ArrayList;
import java.util.List;

import es.esimarket.backend.controllers.requests.FiltroPedRequest;
import es.esimarket.backend.controllers.requests.TaquillaRequest;
import es.esimarket.backend.entities.FotoProd;
import es.esimarket.backend.exceptions.CannotCompleteActionError;
import es.esimarket.backend.exceptions.CannotCompletePurchaseError;
import es.esimarket.backend.exceptions.CannotCreatePhotoError;
import es.esimarket.backend.exceptions.CannotCreateProductError;
import es.esimarket.backend.repositories.CompraRepository;
import es.esimarket.backend.repositories.FotoProdRepository;
import es.esimarket.backend.repositories.PedidosRepository;
import es.esimarket.backend.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
    private FotoProdRepository fotoProdRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidosMapper pedidosMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<PedidosDTO> filtro_pedidos(String dni, FiltroPedRequest request){

        StringBuilder sql = new StringBuilder("SELECT * FROM pedido");
        sql.append("JOIN compra c ON p.IdCompra = c.IdCompra ");
        sql.append("JOIN producto pr ON c.IDproducto = pr.ID ");
        List<Object> params = new ArrayList<>();

        String[] filters = new String[]{"todos", "por entregar","por recoger"};

        if ( request.filter() != null && ( request.filter().equals("todos") || request.filter().equals("por entregar") || request.filter().equals("por recoger") ) ){
            switch (request.filter()) {
                case "por entregar":
                    // El usuario es el VENDEDOR y el estado es 'PorEntregar'
                    sql.append("WHERE pr.uDNIVendedor = ? AND p.Estado = 'PorEntregar'");
                    params.add(dni);
                    break;

                case "por recoger":
                    // El usuario es el COMPRADOR y el estado es 'Entregado' (listo para recoger)
                    sql.append("WHERE c.uDNIcomprador = ? AND p.Estado = 'Entregado'");
                    params.add(dni);
                    break;

                case "todos":
                default:
                    // El usuario es el COMPRADOR O el VENDEDOR
                    sql.append("WHERE c.uDNIcomprador = ? OR pr.uDNIVendedor = ?");
                    params.add(dni);
                    params.add(dni); // Añadimos el DNI dos veces para los dos '?'
                    break;
            }

        }


        List<Pedidos> peds = jdbcTemplate.query(String.valueOf(sql), new BeanPropertyRowMapper<>(Pedidos.class), params.toArray());
        List<PedidosDTO> PedidosDTOs = new ArrayList<>();

        for( Pedidos p : peds)
        {
            Compra c = compraRepository.findById(p.getIdCompra()).orElseThrow(()->new CannotCompletePurchaseError("Compra no encontrada"));
            Producto prod = productoRepository.findById(c.getIDProducto()).orElseThrow(()->new CannotCreateProductError("Producto no encontrado"));
            FotoProd fp = fotoProdRepository.findById(prod.getID()).orElseThrow(()->new CannotCreatePhotoError("Foto no encontrada"));

            PedidosDTOs.add(new PedidosDTO(p.getIdPedido(),fp.getFoto(),c.getuDNIComprador(),prod.getuDNI_Vendedor(),prod.getNombre(),p.getNTaquilla(),p.isEnTaquilla(),p.getEstado()));
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