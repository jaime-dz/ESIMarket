package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.Producto;
import java.io.Serializable;
import java.time.LocalDateTime;

public class CompraDTO implements Serializable {

    private Integer idCompra;
    private Integer idProd;
    private String nombreProd;
    private String fecha;
    private Producto.PagoAceptado tipoPago;
    private Producto.RecepcionAceptada recepcion;
    private String nombreProdTrueque;
    private String tipoProd;

    public CompraDTO () {}

    public CompraDTO(Integer idCompra,Integer idProd, String nombreProd, String fecha, Producto.PagoAceptado tipoPago, Producto.RecepcionAceptada recepcion, String nombreProdTrueque, String tipoProd) {
        this.idCompra = idCompra;
        this.idProd = idProd;
        this.nombreProd = nombreProd;
        this.fecha = fecha;
        this.tipoPago = tipoPago;
        this.recepcion = recepcion;
        this.nombreProdTrueque = nombreProdTrueque;
        this.tipoProd = tipoProd;
    }

    public Integer getIdCompra() {return idCompra;}
    public Integer getIdProd() {return idProd;}
    public String getNombreProd() {return nombreProd;}
    public String getFecha() {return fecha;}
    public Producto.PagoAceptado getTipoPago() {return tipoPago;}
    public Producto.RecepcionAceptada getRecepcion() {return recepcion;}
    public String getNombreProdTrueque() {return nombreProdTrueque;}
    public String getTipoProd() {return tipoProd;}

    public void setIdCompra(Integer idCompra) {this.idCompra = idCompra;}
    public void setIdProd(Integer idProd) {this.idProd = idProd;}
    public void setNombreProd(String nombreProd) {this.nombreProd = nombreProd;}
    public void setFecha(String fecha) {this.fecha = fecha;}
    public void setTipoPago(Producto.PagoAceptado tipoPago) {this.tipoPago = tipoPago;}
    public void setRecepcion(Producto.RecepcionAceptada recepcion) {this.recepcion = recepcion;}
    public void setNombreProdTrueque(String nombreProdTrueque) {this.nombreProdTrueque = nombreProdTrueque;}
    public void setTipoProd(String tipoProd) {this.tipoProd = tipoProd;}

    @Override
    public String toString() {
        return "CompraDTO{" +
                "idCompra=" + idCompra +
                ", idProd=" + idProd +
                ", nombreProd='" + nombreProd + '\'' +
                ", fecha='" + fecha + '\'' +
                ", tipoPago=" + tipoPago +
                ", recepcion=" + recepcion +
                ", nombreProdTrueque='" + nombreProdTrueque + '\'' +
                ", tipoProd='" + tipoProd + '\'' +
                '}';
    }
}
