package es.esimarket.backend.entities.id;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MensajeId implements Serializable {

    @Column(name= "ID_Chat", nullable = false)
    private int ID_Chat;

    @Column(name= "uDNI_remitente", nullable = false)
    private String uDNI_remitente;

    @Column(name = "FechaHora", nullable = false)
    private String FechaHora;

    public MensajeId() {}

    public MensajeId(int ID_Chat, String uDNI_remitente, String FechaHora) {
        this.ID_Chat = ID_Chat;
        this.uDNI_remitente = uDNI_remitente;
        this.FechaHora = FechaHora;
    }


    public int getID_Chat() { return ID_Chat; }
    public void setID_Chat(int ID_Chat) { this.ID_Chat = ID_Chat; }

    public String getuDNI_remitente() { return uDNI_remitente; }
    public void setuDNI_remitente(String uDNI_remitente) {  this.uDNI_remitente = uDNI_remitente; }

    public String getFechaHora() { return FechaHora; }
    public void setFechaHora(String FechaHora) { this.FechaHora = FechaHora; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MensajeId that)) return false;
        return Objects.equals(ID_Chat, that.ID_Chat) &&
                Objects.equals(uDNI_remitente, that.uDNI_remitente) &&
                Objects.equals(FechaHora, that.FechaHora);


    }

    @Override
    public int hashCode() {
        return Objects.hash(ID_Chat, uDNI_remitente, FechaHora);
    }
}
