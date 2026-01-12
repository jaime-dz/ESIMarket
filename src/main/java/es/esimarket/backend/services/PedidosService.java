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

        StringBuilder sql = new StringBuilder("SELECT p.IdPedido, p.IdProd, p.Estado, p.EnTaquilla AS enTaquilla,p.NumTaquilla AS NTaquilla ");
        sql.append("FROM pedido p ");
        sql.append("JOIN compra c ON (c.IDproducto = p.IdProd OR c.IdProdTrueque = p.IdProd) ");
        sql.append("JOIN producto pr ON c.IDproducto = pr.ID ");
        sql.append("WHERE (c.uDNIcomprador = ? OR pr.uDNIVendedor = ?) ");

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

        for (Pedidos p : peds) {

            System.out.println("-----------------------------------\n" + p.getIdProd());

            Producto prodActual = productoRepository.findById(p.getIdProd())
                    .orElseThrow(() -> new CannotCreateProductError("Producto no encontrado"));

            Compra c = compraRepository.findByIDProductoOrIDProdTrueque(p.getIdProd(), p.getIdProd());

            if (c == null) continue;

            Usuario usuarioMainComprador = usuarioRepository.findByid(c.getuDNIComprador());

            Producto prodPrincipal = productoRepository.findById(c.getIDProducto()).orElse(null);
            Usuario usuarioMainVendedor = (prodPrincipal != null) ? usuarioRepository.findByid(prodPrincipal.getuDNI_Vendedor()) : null;

            if (usuarioMainComprador == null || usuarioMainVendedor == null) continue;

            String nombreCompradorFinal;
            String nombreVendedorFinal;
            String nombreProductoFinal = prodActual.getNombre();
            Boolean esCompradorFinal;
            Integer NTaq = (p.getNTaquilla() != null) ? p.getNTaquilla() : null;

            if (prodActual.getID() == c.getIDProducto()) {
                nombreCompradorFinal = usuarioMainComprador.getNombre();
                nombreVendedorFinal = usuarioMainVendedor.getNombre();
                esCompradorFinal = usuarioMainComprador.getId().equals(dni);

            } else {

                nombreCompradorFinal = usuarioMainVendedor.getNombre();
                nombreVendedorFinal = usuarioMainComprador.getNombre();
                esCompradorFinal = usuarioMainVendedor.getId().equals(dni);
            }

            PedidosDTOs.add(new PedidosDTO(
                    p.getIdPedido(),
                    nombreCompradorFinal,
                    nombreVendedorFinal,
                    esCompradorFinal,
                    nombreProductoFinal,
                    NTaq,
                    p.isEnTaquilla(),
                    p.getEstado()
            ));
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
        Compra c = compraRepository.findByIDProductoOrIDProdTrueque(ped.getIdProd(),ped.getIdProd());
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
        Compra c = compraRepository.findByIDProductoOrIDProdTrueque(p.getIdProd(),p.getIdProd());
        Producto prod = productoRepository.findById(c.getIDProducto()).orElseThrow( () -> new CannotCompleteActionError("Producto no encontrado") );

        if ( !c.getuDNIComprador().equals(dni) ) throw new CannotCompleteActionError("Debes ser el comprador del producto para poder recogerlo");
        p.setEstado(Pedidos.Estado.Recogido);

        pedidosRepository.save(p);

        return "Se ha recogido el pedido con exito";
    }


}