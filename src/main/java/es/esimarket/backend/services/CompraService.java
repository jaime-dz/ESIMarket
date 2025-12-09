package es.esimarket.backend.services;
import es.esimarket.backend.controllers.requests.CompraRequest;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.exceptions.CannotCompletePurchaseError;
import es.esimarket.backend.repositories.ProductoRepository;
import es.esimarket.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.repositories.CompraRepository;
import es.esimarket.backend.repositories.PedidosRepository;
import es.esimarket.backend.entities.Compra;
import es.esimarket.backend.entities.Pedidos;

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
    public CompraRepository compraRepository;

    @Autowired
    public VariosService variosService;

    public Boolean UsrPuedeHacerCompra(Usuario u, Producto p)
    {
        return u.getSaldoMoneda() > p.getPrecio();
    }

    public String HacerCompra(String uDNI, CompraRequest request)
    {
        Producto p = productoRepository.findByID(request.idProd());
        Usuario u = usuarioRepository.findByid(uDNI);
        Compra c = new Compra();
        String FechaAct = variosService.ObtenerFecha();

        if ( p != null && u != null) {

            if ( p.getuDNI_Vendedor().equals(uDNI) ){
                throw new CannotCompletePurchaseError("No puedes comprar tu propio producto ;)");
            }

            if ( request.tipoPago() == Producto.PagoAceptado.Monedas){

                if ( u.getSaldoMoneda() < p.getPrecio() ) {
                    throw new CannotCompletePurchaseError("No tienes saldo para comprar este producto");
                }

                u.setSaldoMoneda(u.getSaldoMoneda() -  p.getPrecio());
                usuarioRepository.save(u);

                c = new Compra(uDNI,request.idProd(),FechaAct,request.recepcion(),request.tipoPago());


            }else if ( request.tipoPago() == Producto.PagoAceptado.Trueque ){

                //c = new Compra(uDNI,request.idProd(),variosService.ObtenerFecha(),request.recepcion(),request.tipoPago(), p.getID(), request.idProdOfrecido());


            }else throw new CannotCompletePurchaseError("Tipo de pago no encontrado");

            compraRepository.save(c);

            if(request.recepcion()==Producto.RecepcionAceptada.Taquilla)
            {
                Pedidos pe = new Pedidos(c.getIDCompra(),request.taquilla(),Pedidos.Estado.PorEntregar);
                pedidosRepository.save(pe);
            }
            

            return "Compra realizada correctamente";
        }

        throw new CannotCompletePurchaseError("No se pudo encontrar el usuario o producto");

    }

}