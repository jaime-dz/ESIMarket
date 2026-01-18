package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.Notificacion;

import java.io.Serializable;
import java.math.BigInteger;

public class NotificacionDTO implements Serializable {

    private BigInteger id;
    private String uDNI;
    private String mensaje;

    public NotificacionDTO() {}

    public NotificacionDTO(Notificacion n){
        this.id = n.getId();
        this.uDNI = n.getuDNI();
        this.mensaje = n.getMensaje();
    }

    public NotificacionDTO(BigInteger id, String uDNI, String mensaje) {
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
        return "NotificacionDTO{" +
                "id=" + id +
                ", uDNI='" + uDNI + '\'' +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }

}
