package es.esimarket.backend.services;
import es.esimarket.backend.controllers.requests.CompraRequest;
import es.esimarket.backend.entities.*;
import es.esimarket.backend.exceptions.CannotCompletePurchaseError;
import es.esimarket.backend.repositories.ProductoRepository;
import es.esimarket.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import es.esimarket.backend.repositories.CompraRepository;
import es.esimarket.backend.repositories.PedidosRepository;

import java.util.*;

@Service
public class CompraService {

    @Autowired
    public ProductoRepository productoRepository;

    @Autowired
    public PedidosRepository pedidosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServicioService servicioService;

    @Autowired
    public CompraRepository compraRepository;

    @Autowired
    public VariosService variosService;

    public Boolean UsuPuedeHacerCompra(Usuario u, Producto p)
    {
        return u.getSaldoMoneda() >= p.getPrecio();
    }

    public String HacerCompra(String uDNI, CompraRequest request)
    {
        Producto p = productoRepository.findByID(request.idProd());
        Usuario uComprador = usuarioRepository.findByid(uDNI);
        Usuario uVendedor = usuarioRepository.findByid(p.getuDNI_Vendedor());
        Compra c = null;
        String FechaAct = variosService.ObtenerFecha();

        if ( p != null && uComprador != null && uVendedor != null) {

            if ( p.getuDNI_Vendedor().equals(uDNI) ){
                throw new CannotCompletePurchaseError("No puedes comprar tu propio producto ;)");
            }

            if ( request.tipoPago() == Producto.PagoAceptado.Monedas){

                if ( !UsuPuedeHacerCompra(uComprador,p) ) {
                    throw new CannotCompletePurchaseError("No tienes saldo para comprar este producto");
                }

                c = new Compra(uDNI,request.idProd(),FechaAct,request.recepcion(),request.tipoPago());


            }else if ( request.tipoPago() == Producto.PagoAceptado.Trueque ){

                c = new Compra(uDNI,request.idProd(),FechaAct,request.recepcion(),request.tipoPago(), request.idProdTrueque());


            }else throw new CannotCompletePurchaseError("Tipo de pago no encontrado");

            compraRepository.save(c);

            if ( p.getTipo().equals("Objeto")){
                Pedidos pe = getPedidos(request, p, c);

                pedidosRepository.save(pe);
            }else if ( p.getTipo().equals("Servicio")){

                servicioService.CrearServicioPendiente(p.getID(),uComprador.getId());
            }else throw new CannotCompletePurchaseError("Tipo de producto invalido");

            uComprador.setSaldoMoneda(uComprador.getSaldoMoneda() -  p.getPrecio());
            uVendedor.setSaldoMoneda(uVendedor.getSaldoMoneda() +  p.getPrecio());
            usuarioRepository.save(uComprador);
            usuarioRepository.save(uVendedor);

            return "Compra realizada correctamente";
        }

        throw new CannotCompletePurchaseError("No se pudo encontrar el usuario o producto");

    }

    private static Pedidos getPedidos(CompraRequest request, Producto p, Compra c) {
        Pedidos pe = null;

        if ( request.recepcion() != p.getRecepcionAceptada() )
            throw new CannotCompletePurchaseError("Tipo de recepcion invalida");
        if(request.recepcion()==Producto.RecepcionAceptada.enTaquilla)
        {
            pe = new Pedidos(c.getIDCompra(),Pedidos.Estado.PorEntregar);

        }else if ( request.recepcion()==Producto.RecepcionAceptada.EnMano){

            pe = new Pedidos(c.getIDCompra(),Pedidos.Estado.PorEntregar);
        }else throw new CannotCompletePurchaseError("Tipo de recepcion no encontrado");
        return pe;
    }

}