package es.esimarket.backend.entities;
import jakarta.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name="notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private BigInteger id;

    @Column(name="uDNI")
    private String uDNI;

    @Column(name="mensaje")
    private String mensaje;

    public Notificacion() {}

    public Notificacion(BigInteger id, String uDNI, String mensaje) {
        this.id = id;
        this.uDNI = uDNI;
        this.mensaje = mensaje;
    }

    public BigInteger getId() {return id;}
    public void setId(BigInteger id) {this.id = id;}

    public String getuDNI() {return uDNI;}
    public void setuDNI(String uDNI) {this.uDNI = uDNI;}

    public String getMensaje() {return mensaje;}
    public void setMensaje(String mensaje) {this.mensaje = mensaje;}


    @Override
    public String toString() {
        return "Notificacion{" +
                "id=" + id +
                ", uDNI='" + uDNI + '\'' +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }
}



