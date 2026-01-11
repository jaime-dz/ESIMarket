package es.esimarket.backend.services;
import java.util.ArrayList;
import java.util.List;

import es.esimarket.backend.controllers.requests.FiltroPedRequest;
import es.esimarket.backend.controllers.requests.TaquillaRequest;
import es.esimarket.backend.entities.*;
import es.esimarket.backend.exceptions.CannotCompleteActionError;
import es.esimarket.backend.exceptions.CannotCompletePurchaseError;
import es.esimarket.backend.exceptions.CannotCreatePhotoError;
import es.esimarket.backend.exceptions.CannotCreateProductError;
import es.esimarket.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import es.esimarket.backend.dtos.PedidosDTO;
import es.esimarket.backend.mappers.PedidosMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidosService{

    @Autowired
    private PedidosRepository pedidosRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FotoProdRepository fotoProdRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidosMapper pedidosMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<PedidosDTO> filtro_pedidos(String dni, FiltroPedRequest request){

        StringBuilder sql = new StringBuilder("SELECT p.IdPedido, p.IdCompra, p.Estado,p.EnTaquilla AS enTaquilla,p.NumTaquilla AS NTaquilla, pr.uDNIVendedor, c.uDNIcomprador FROM pedido p ");
        sql.append("JOIN compra c ON p.IdCompra = c.IdCompra ");
        sql.append("JOIN producto pr ON c.IDproducto = pr.ID WHERE ( c.uDNIcomprador = ? OR pr.uDNIVendedor = ? )");
        List<Object> params = new ArrayList<>();

        params.add(dni);
        params.add(dni);

        String[] filters = new String[]{"Todos", "PorEntregar","Entregado", "Recogido"};

        if ( request.filter() != null && ( request.filter().equals("Todos") || request.filter().equals("PorEntregar") || request.filter().equals("Entregado") || request.filter().equals("Recogido") ) ){
            switch (request.filter()) {
                case "PorEntregar":
                    // El usuario es el VENDEDOR y el estado es 'PorEntregar'
                    sql.append("AND  p.Estado = 'PorEntregar'");
                    break;

                case "Entregado":
                    // El usuario es el COMPRADOR y el estado es 'Entregado' (listo para recoger)
                    sql.append("AND p.Estado = 'Entregado'");
                    break;

                case "Recogido":
                    sql.append("AND p.Estado = 'Recogido'");
                    break;
                default:
                    break;
            }

        }

        List<Pedidos> peds = jdbcTemplate.query(String.valueOf(sql), new BeanPropertyRowMapper<>(Pedidos.class), params.toArray());
        List<PedidosDTO> PedidosDTOs = new ArrayList<>();

        for( Pedidos p : peds)
        {
            Producto prod = productoRepository.findById(p.getIdProd()).orElseThrow(()->new CannotCreateProductError("Producto no encontrado"));
            Compra c = compraRepository.findByIDProducto(p.getIdProd());
            Usuario uC = usuarioRepository.findByid(c.getuDNIComprador());
            Usuario uV = usuarioRepository.findByid(prod.getuDNI_Vendedor());
            Integer NTaq = null;
            String nombreComprador = "Anónimo";
            String nombreVendedor = "Anónimo";
            String nombreProd = "Desconocido";
            boolean esComprador = false;

            if (p.getNTaquilla() != null) {
                NTaq=p.getNTaquilla();
            }

            if ( c.getIDProdTrueque() != null ){
                Producto prodT = productoRepository.findById(c.getIDProdTrueque()).orElseThrow(()->new CannotCreateProductError("Producto no encontrado"));

                if ( prod.getID() == c.getIDProducto() ){

                    nombreComprador = uC.getNombre();
                    nombreVendedor = uV.getNombre();
                    esComprador = c.getuDNIComprador().equals(dni);
                    nombreProd = prod.getNombre();

                }else if ( prod.getID() == c.getIDProdTrueque() ){

                    nombreComprador = uV.getNombre();
                    nombreVendedor = uC.getNombre();
                    esComprador = !c.getuDNIComprador().equals(dni);
                    nombreProd = prodT.getNombre();

                }
            }


            PedidosDTOs.add(new PedidosDTO(p.getIdPedido(),nombreComprador,nombreVendedor,esComprador,nombreProd,NTaq,p.isEnTaquilla(),p.getEstado()));
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
        Compra c = compraRepository.findByIDProducto(ped.getIdProd());
        Producto p = productoRepository.findById(c.getIDProducto()).orElseThrow( () -> new CannotCompleteActionError("Producto no encontrada"));
        if ( !p.getuDNI_Vendedor().equals(dni) ) throw new CannotCompleteActionError("Debes ser propietario del producto para entregarlo");
        ped.setEstado(Pedidos.Estado.Entregado);
        if ( c.getRecepcion() == Producto.RecepcionAceptada.enTaquilla ) ped.setNTaquilla(NTaquilla);

        pedidosRepository.save(ped);

        if ( c.getRecepcion() == Producto.RecepcionAceptada.enMano && c.getTipoPago() == Producto.PagoAceptado.Trueque ){
            Producto pT = productoRepository.findById(c.getIDProdTrueque()).orElseThrow( () -> new CannotCompleteActionError("Producto no encontrado") );
        }

        return "Se ha entregado su pedido con exito";
    }

    @Transactional
    public String recogerPedido(int IdPedido, String dni )
    {
        Pedidos p = pedidosRepository.findById(IdPedido).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Compra c = compraRepository.findByIDProducto(p.getIdProd());
        Producto prod = productoRepository.findById(c.getIDProducto()).orElseThrow( () -> new CannotCompleteActionError("Producto no encontrado") );

        if ( !c.getuDNIComprador().equals(dni) ) throw new CannotCompleteActionError("Debes ser el comprador del producto para poder recogerlo");
        p.setEstado(Pedidos.Estado.Recogido);

        pedidosRepository.save(p);

        return "Se ha recogido el pedido con exito";
    }


}