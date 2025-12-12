package es.esimarket.backend.entities;
import java.math.BigInteger;

import jakarta.persistence.*;

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IdCompra")
    private Integer IDCompra;

    @Column(name = "uDNIComprador", nullable = false)
    private String uDNIComprador;

    @Column(name = "IDProducto", nullable = false)
    private int IDProducto;

    @Column(name = "Fecha", nullable = false)
    private String Fecha;

    @Column(name = "TipoPago")
    @Enumerated(EnumType.STRING)
    private Producto.PagoAceptado TipoPago;

    @Column(name = "Recepcion")
    @Enumerated(EnumType.STRING)
    private Producto.RecepcionAceptada recepcion;

    @Column(name = "IdProdTrueque", nullable = true)
    private Integer idProdTrueque;

    public Compra() {}

    public Compra(String uDNIComprador, int IDProducto, String Fecha, Producto.RecepcionAceptada recepcion, Producto.PagoAceptado TipoPago, int idProdTrueque) {
        this.uDNIComprador = uDNIComprador;
        this.IDProducto = IDProducto;
        this.Fecha = Fecha;
        this.recepcion = recepcion;
        this.TipoPago = TipoPago;
        this.idProdTrueque = idProdTrueque;
    }

    public Compra(String uDNIComprador, int IDProducto, String Fecha, Producto.RecepcionAceptada recepcion, Producto.PagoAceptado TipoPago) {
        this.uDNIComprador = uDNIComprador;
        this.IDProducto = IDProducto;
        this.Fecha = Fecha;
        this.recepcion = recepcion;
        this.TipoPago = TipoPago;
    }

    public String getuDNIComprador() {return uDNIComprador;}
    public void setuDNIComprador(String uDNIComprador) { this.uDNIComprador = uDNIComprador; }

    public int getIDProducto(){return IDProducto;}
    public void setIDProducto(int IDProducto) {this.IDProducto = IDProducto; }

    public String getFecha() {return Fecha;}
    public void setFecha(String Fecha) {this.Fecha = Fecha; }

    public Producto.RecepcionAceptada getRecepcion() {return recepcion;}
    public void setRecepcion(Producto.RecepcionAceptada recepcion) {this.recepcion=recepcion;}

    public Integer getIDCompra() {return IDCompra;}
    public void setIDCompra(Integer IDCompra) {this.IDCompra=IDCompra;}

    public Producto.PagoAceptado getTipoPago() {return TipoPago;}
    public void setTipoPago(Producto.PagoAceptado tipoPago) {TipoPago = tipoPago;}

    public Integer getIdProdTrueque() {return idProdTrueque;}
    public void setIdProdTrueque(Integer idProdTrueque) {this.idProdTrueque = idProdTrueque;}
}