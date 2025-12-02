package es.esimarket.backend.entities;
import java.math.BigInteger;

import es.esimarket.backend.entities.id.CompraId;
import jakarta.persistence.*;

@Entity
@Table(name = "compra")
public class Compra {

    @EmbeddedId
    private CompraId id;

    private String uDNIComprador;

    private int IDProducto;

    private String Fecha;

    
    private BigInteger iDCompra;

    private Producto.RecepcionAceptada recepcion;

    public Compra() {}

    public Compra( String uDNIComprador, int IDProducto, String Fecha, Producto.RecepcionAceptada recepcion) {
        //this.id = new CompraId(uDNIComprador,IDProducto,Fecha);
        this.uDNIComprador = uDNIComprador;
        this.IDProducto = IDProducto;
        this.Fecha = Fecha;
        this.recepcion = recepcion;
    }

    public String getuDNIComprador() {return id.getuDNIComprador();}
    public void setuDNIComprador(String uDNIComprador) { id.setuDNIComprador(uDNIComprador); }

    public int getIDProducto(){return id.getIDProducto();}
    public void setIDProducto(int IDProducto) {id.setIDProducto(IDProducto); }

    public String getFecha() {return id.getFecha();}
    public void setFecha(String Fecha) {id.setFecha(Fecha);}

    public Producto.RecepcionAceptada getRecepcion() {return recepcion;}
    public void setRecepcion(Producto.RecepcionAceptada recepcion) {this.recepcion=recepcion;}

    public BigInteger getidCompra() {return iDCompra;}
    public void setiDCompra(BigInteger iDCompra) {this.iDCompra=iDCompra;}


}