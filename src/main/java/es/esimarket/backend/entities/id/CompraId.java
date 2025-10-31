package es.esimarket.backend.entities.id;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CompraId implements Serializable {

    @Column(name = "uDNI_comprador", nullable = false)
    private String uDNI_comprador;

    @Column(name = "ID_producto", nullable = false)
    private int ID_producto;

    @Column(name = "Fecha", nullable = false)
    private String Fecha;

    public CompraId() {}

    public CompraId(String uDNI_comprador, int ID_producto, String Fecha) {
        this.uDNI_comprador = uDNI_comprador;
        this.ID_producto = ID_producto;
        this.Fecha = Fecha;
    }

    public String getUDNI_comprador() { return uDNI_comprador; }
    public void setUDNI_comprador(String uDNI_comprador) { this.uDNI_comprador = uDNI_comprador; }

    public int getID_producto() { return ID_producto; }
    public void setID_producto(int ID_producto) { this.ID_producto = ID_producto; }

    public String getFecha() { return Fecha; }
    public void setFecha(String Fecha) { this.Fecha = Fecha; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompraId that)) return false;
        return Objects.equals(uDNI_comprador, that.uDNI_comprador) &&
                Objects.equals(ID_producto, that.ID_producto) &&
                Objects.equals(Fecha, that.Fecha);


    }

    @Override
    public int hashCode() {
        return Objects.hash(uDNI_comprador, ID_producto, Fecha);
    }

}
