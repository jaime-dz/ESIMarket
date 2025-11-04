package es.esimarket.backend.entities.id;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CompraId implements Serializable {

    @Column(name = "uDNIComprador", nullable = false)
    private String uDNIComprador;

    @Column(name = "IDProducto", nullable = false)
    private int IDProducto;

    @Column(name = "Fecha", nullable = false)
    private String Fecha;

    public CompraId() {}

    public CompraId(String uDNIComprador, int IDProducto, String Fecha) {
        this.uDNIComprador = uDNIComprador;
        this.IDProducto = IDProducto;
        this.Fecha = Fecha;
    }

    public String getuDNIComprador() { return uDNIComprador; }
    public void setuDNIComprador(String uDNIComprador) { this.uDNIComprador = uDNIComprador; }

    public int getIDProducto() { return IDProducto; }
    public void setIDProducto(int IDProducto) { this.IDProducto = IDProducto; }

    public String getFecha() { return Fecha; }
    public void setFecha(String Fecha) { this.Fecha = Fecha; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompraId that)) return false;
        return Objects.equals(uDNIComprador, that.uDNIComprador) &&
                Objects.equals(IDProducto, that.IDProducto) &&
                Objects.equals(Fecha, that.Fecha);


    }

    @Override
    public int hashCode() {
        return Objects.hash(uDNIComprador, IDProducto, Fecha);
    }

}
