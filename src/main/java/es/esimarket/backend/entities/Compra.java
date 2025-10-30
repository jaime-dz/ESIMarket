package es.esimarket.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @Column(name= "uDNI_comprador", nullable = false)
    private String uDNI_comprador;

    @Column(name= "ID_producto", nullable = false)
    private int ID_producto;

    @Column(name= "Fecha",nullable = false)
    private String Fecha;

    public Compra() {}

    public Compra(String uDNI_comprador,int ID_producto,String Fecha)
    {
        this.uDNI_comprador=uDNI_comprador;
        this.ID_producto=ID_producto;
        this.Fecha=Fecha;
    }

    public String getuDNI_comprador() {return uDNI_comprador;}
    public void setuDNI_comprador(String uDNI_comprador) {this.uDNI_comprador=uDNI_comprador;}

    public int getID_producto(){return ID_producto;}
    public void setID_producto(int ID_producto) {this.ID_producto=ID_producto;}

    public String getFecha() {return Fecha;}
    public void setFecha(String Fecha) {this.Fecha=Fecha;}

}