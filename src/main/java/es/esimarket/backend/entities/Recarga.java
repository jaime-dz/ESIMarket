package es.esimarket.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="recarga")
public class Recarga{

    public enum EstadoPedido {
        PENDIENTE,
        EN_PROCESO,
        ENTREGADO,
        CANCELADO
    }

    @Id
    @Column(name="IDTransaccion",nullable = false)
    private int ID_Transaccion;

    @Column(name = "uDNIComprador",nullable = false)
    private String uDNI_Comprador;

    @Column(name = "IDPaquete",nullable = false)
    private int ID_Paquete;

    @Column(name = "PrecioPagado",nullable = false)
    private double Precio_Pagado;

    @Column(name = "MonedasObtenidas",nullable = false)
    private int  Monedas_Obtenidas;

    @Column(name = "FechaHora")
    private String FechaHora;

    @Column(name = "Estado",nullable = false)
    private EstadoPedido Estado;


    public Recarga(){}

    public Recarga(int ID_Transaccion)
    {this.ID_Transaccion=ID_Transaccion;}

    public int getID_Transaccion(){return ID_Transaccion;}
    public void setID_Transaccion(int ID_Transaccion){this.ID_Transaccion=ID_Transaccion;}

    public String getUDNI_Comprador(){return uDNI_Comprador;}
    public void setuDNI_Comprador(String uDNI_Comprador){this.uDNI_Comprador=uDNI_Comprador;}

    public int getID_Paquete(){return ID_Paquete;}
    public void setID_Paquete(int ID_Paquete){this.ID_Paquete=ID_Paquete;}

    public double getPrecio_Pagado(){return Precio_Pagado;}
    public void setPrecio_Pagado(double Precio_Pagado){this.Precio_Pagado=Precio_Pagado;}

    public int getMonedas_Obtenidas(){return Monedas_Obtenidas;}
    public void setMonedas_Obtenidas(int Monedas_Obtenidas){ this.Monedas_Obtenidas=Monedas_Obtenidas; }

    public String getFechaHora(){return FechaHora;}
    public void setFechaHora(String FechaHora){this.FechaHora=FechaHora;}

    public EstadoPedido getEstado(){return Estado;}
    public void setEstado(EstadoPedido Estado){this.Estado=Estado;}

}