package es.esimarket.backend.services;
import es.esimarket.backend.controllers.requests.CompraRequest;
import es.esimarket.backend.dtos.CompraDTO;
import es.esimarket.backend.dtos.ProductoDTO;
import es.esimarket.backend.entities.*;
import es.esimarket.backend.exceptions.CannotCompletePurchaseError;
import es.esimarket.backend.exceptions.CannotCreateProductError;
import es.esimarket.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CompraService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidosRepository pedidosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private VariosService variosService;

    public List<CompraDTO> mostrar_compras_usu(String dni){

        List<Compra> comprasEntities = compraRepository.findByuDNIComprador(dni);
        List<CompraDTO> compraDTOS = new ArrayList<>();

        for ( Compra c : comprasEntities ){

            Producto pC = productoRepository.findByID(c.getIDProducto());

            String nombreTrueque = null;
            if (c.getIDProdTrueque() != null) {
                Producto pT = productoRepository.findByID(c.getIDProdTrueque());
                if (pT != null) {
                    nombreTrueque = pT.getNombre();
                }
            }

            compraDTOS.add(new CompraDTO(c.getIDCompra(),c.getIDProducto(),pC.getNombre(),c.getFecha(),c.getTipoPago(),c.getRecepcion(),nombreTrueque,pC.getTipo()));
        }

        return compraDTOS;
    }

    public Boolean UsuPuedeHacerCompra(Usuario u, Producto p)
    {
        return u.getSaldoMoneda() >= p.getPrecio();
    }

    @Transactional
    public String HacerCompra(String uDNI, CompraRequest request)
    {
        Producto p = productoRepository.findByID(request.idProd());
        Producto pT = null;
        Usuario uComprador = usuarioRepository.findByid(uDNI);
        Usuario uVendedor = usuarioRepository.findByid(p.getuDNI_Vendedor());
        Compra c = null;
        LocalDateTime FechaAct = variosService.ObtenerFecha();

        if ( p != null && uComprador != null && uVendedor != null) {

            if ( p.getuDNI_Vendedor().equals(uDNI) ){
                throw new CannotCompletePurchaseError("No puedes comprar tu propio producto ;)");
            }

            if ( request.tipoPago() == Producto.PagoAceptado.Monedas){

                if ( !UsuPuedeHacerCompra(uComprador,p) ) {
                    throw new CannotCompletePurchaseError("No tienes saldo para comprar este producto");
                }

                c = new Compra(uDNI,request.idProd(),FechaAct,request.recepcion(),request.tipoPago());
                compraRepository.save(c);

                if ( p.getTipo().equals("Objeto")){
                    Pedidos pe = getPedidos(request, p);
                    pedidosRepository.save(pe);

                    uComprador.setSaldoMoneda(uComprador.getSaldoMoneda() -  p.getPrecio());
                    uVendedor.setSaldoMoneda(uVendedor.getSaldoMoneda() +  p.getPrecio());
                }else if ( p.getTipo().equals("Servicio")){

                    Servicio s = servicioService.CrearServicioPendiente(p.getID(),uComprador.getId());
                    servicioRepository.save(s);

                    uComprador.setSaldoMoneda(uComprador.getSaldoMoneda() -  (p.getPrecio()*request.horas()));
                    uVendedor.setSaldoMoneda(uVendedor.getSaldoMoneda() +  (p.getPrecio()*request.horas()));

                }else throw new CannotCompletePurchaseError("Tipo de producto invalido");

            }else if ( request.tipoPago() == Producto.PagoAceptado.Trueque ){

                c = new Compra(uDNI,request.idProd(),FechaAct,request.recepcion(),request.tipoPago(), request.idProdTrueque());
                compraRepository.save(c);

                pT = productoRepository.findById(request.idProdTrueque()).orElseThrow(()->new CannotCreateProductError("Producto no encontrado"));

                if ( pT.getTipo().equals("Objeto")){
                    Pedidos peT = getPedidos(new CompraRequest(pT.getID(),pT.getPagoAceptado(),pT.getRecepcionAceptada(),null,null), pT);
                    Pedidos pe = getPedidos(new CompraRequest(p.getID(),p.getPagoAceptado(),p.getRecepcionAceptada(),null,null), p);
                    pedidosRepository.save(peT);
                    pedidosRepository.save(pe);
                }else if ( pT.getTipo().equals("Servicio") ){
                    Servicio s = servicioService.CrearServicioPendiente(pT.getID(),p.getuDNI_Vendedor());
                    servicioRepository.save(s);
                }else throw new CannotCompletePurchaseError("Tipo prodcuto invalido");


            }else throw new CannotCompletePurchaseError("Tipo de pago no encontrado");

            p.setDisponible(false);
            if ( pT != null ) {
                pT.setDisponible(false);
                productoRepository.save(pT);
            }
            productoRepository.save(p);

            usuarioRepository.save(uComprador);
            usuarioRepository.save(uVendedor);

            return "Compra realizada correctamente";
        }

        throw new CannotCompletePurchaseError("No se pudo encontrar el usuario o producto");

    }

    private Pedidos getPedidos(CompraRequest request, Producto p) {
        Pedidos pe = null;

        if ( request.recepcion() != p.getRecepcionAceptada() )
            throw new CannotCompletePurchaseError("Tipo de recepcion invalida");
        if(request.recepcion()==Producto.RecepcionAceptada.enTaquilla)
        {
            pe = new Pedidos(p.getID(),Pedidos.Estado.PorEntregar,true);

        }else if ( request.recepcion()==Producto.RecepcionAceptada.enMano){

            pe = new Pedidos(p.getID(),Pedidos.Estado.PorEntregar,false);
        }else throw new CannotCompletePurchaseError("Tipo de recepcion no encontrado");

        return pe;
    }

}