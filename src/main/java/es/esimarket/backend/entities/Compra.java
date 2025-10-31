package es.esimarket.backend.entities;
import es.esimarket.backend.entities.id.CompraId;
import jakarta.persistence.*;

@Entity
@Table(name = "compra")
public class Compra {

    @EmbeddedId
    private CompraId id;

    public Compra() {}

    public String getuDNI_comprador() {return id.getUDNI_comprador();}
    public void setuDNI_comprador(String uDNI_comprador) { id.setUDNI_comprador(uDNI_comprador); }

    public int getID_producto(){return id.getID_producto();}
    public void setID_producto(int ID_producto) {id.setID_producto(ID_producto); }

    public String getFecha() {return id.getFecha();}
    public void setFecha(String Fecha) {id.setFecha(Fecha);}

}