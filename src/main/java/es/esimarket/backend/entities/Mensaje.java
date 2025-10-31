package es.esimarket.backend.entities;
import es.esimarket.backend.entities.id.MensajeId;
import jakarta.persistence.*;

@Entity
@Table(name="mensaje")
public class Mensaje{

    @EmbeddedId
    private MensajeId id;

    @Column(name= "Texto")
    private String Texto;

    public Mensaje(){}

    public Mensaje(int id_chat, String uDNI_rem, String fecha, String Texto){
        this.id = new MensajeId(id_chat,uDNI_rem,fecha);
        this.Texto = Texto;
    }

    public int getID_Chat(){return id.getID_Chat();}
    public void setID_Chat(int ID_Chat) {id.setID_Chat(ID_Chat); }

    public String getuDNI_remitente(){return id.getuDNI_remitente();}
    public void setuDNI_remitente(String uDNI_remitente) {id.setuDNI_remitente(uDNI_remitente); }

    public String getTexto(){return Texto;}
    public void setTexto(String Texto) {this.Texto=Texto;}

    public String getFechaHora(){return id.getFechaHora();}
    public void setFechaHora(String FechaHora) {id.setFechaHora(FechaHora);}
}
