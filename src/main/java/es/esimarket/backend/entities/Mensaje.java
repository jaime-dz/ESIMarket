package es.esimarket.backend.entities;
import es.esimarket.backend.entities.id.MensajeId;
import jakarta.persistence.*;

@Entity
@Table(name="mensaje")
public class Mensaje{

    @EmbeddedId
    private MensajeId id;

    @Column(name= "Texto")
    private String texto;

    public Mensaje(){}

    public Mensaje(int id_chat, String uDNI_rem, String fecha, String texto){
        this.id = new MensajeId(id_chat,uDNI_rem,fecha);
        this.texto = texto;
    }

    public int getIDChat(){return id.getIDChat();}
    public void setIDChat(int IDChat) {id.setIDChat(IDChat); }

    public String getuDNIremitente(){return id.getuDNIremitente();}
    public void setuDNIremitente(String uDNIremitente) {id.setuDNIremitente(uDNIremitente); }

    public String getTexto(){return texto;}
    public void setTexto(String texto) {this.texto=texto;}

    public String getFechaHora(){return id.getFechaHora();}
    public void setFechaHora(String FechaHora) {id.setFechaHora(FechaHora);}
}
