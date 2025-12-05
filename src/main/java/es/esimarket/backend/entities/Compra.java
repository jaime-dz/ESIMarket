package es.esimarket.backend.entities;
import java.math.BigInteger;

import es.esimarket.backend.entities.id.CompraId;
import jakarta.persistence.*;

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger IDCompra;

    @Column(name = "uDNIComprador", nullable = false)
    private String uDNIComprador;

    @Column(name = "IDProducto", nullable = false)
    private int IDProducto;

    @Column(name = "Fecha", nullable = false)
    private String Fecha;

    private Producto.RecepcionAceptada recepcion;

    public Compra() {}

    public Compra( String uDNIComprador, int IDProducto, String Fecha, Producto.RecepcionAceptada recepcion) {
        this.uDNIComprador = uDNIComprador;
        this.IDProducto = IDProducto;
        this.Fecha = Fecha;
        this.recepcion = recepcion;
    }

    public String getuDNIComprador() {return uDNIComprador;}
    public void setuDNIComprador(String uDNIComprador) { this.uDNIComprador = uDNIComprador; }

    public int getIDProducto(){return IDProducto;}
    public void setIDProducto(int IDProducto) {this.IDProducto = IDProducto; }

    public String getFecha() {return Fecha;}
    public void setFecha(String Fecha) {this.Fecha = Fecha; }

    public Producto.RecepcionAceptada getRecepcion() {return recepcion;}
    public void setRecepcion(Producto.RecepcionAceptada recepcion) {this.recepcion=recepcion;}

    public BigInteger getidCompra() {return IDCompra;}
    public void setiDCompra(BigInteger IDCompra) {this.IDCompra=IDCompra;}


}