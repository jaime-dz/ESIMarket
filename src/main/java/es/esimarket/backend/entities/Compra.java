package es.esimarket.backend.entities;
import es.esimarket.backend.entities.id.CompraId;
import jakarta.persistence.*;

@Entity
@Table(name = "compra")
public class Compra {

    @EmbeddedId
    private CompraId id;

    public Compra() {}

    public Compra( String uDNIComprador, int IDProducto, String Fecha) {
        this.id = new CompraId(uDNIComprador,IDProducto,Fecha);
    }

    public String getuDNIComprador() {return id.getuDNIComprador();}
    public void setuDNIComprador(String uDNIComprador) { id.setuDNIComprador(uDNIComprador); }

    public int getIDProducto(){return id.getIDProducto();}
    public void setIDProducto(int IDProducto) {id.setIDProducto(IDProducto); }

    public String getFecha() {return id.getFecha();}
    public void setFecha(String Fecha) {id.setFecha(Fecha);}

}