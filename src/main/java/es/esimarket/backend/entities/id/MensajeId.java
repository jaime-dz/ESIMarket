package es.esimarket.backend.entities.id;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MensajeId implements Serializable {

    @Column(name= "IDChat", nullable = false)
    private int IDChat;

    @Column(name= "uDNIremitente", nullable = false)
    private String uDNIremitente;

    @Column(name = "FechaHora", nullable = false)
    private String fechaHora;

    public MensajeId() {}

    public MensajeId(int IDChat, String uDNIremitente, String fechaHora) {
        this.IDChat = IDChat;
        this.uDNIremitente = uDNIremitente;
        this.fechaHora = fechaHora;
    }


    public int getIDChat() { return IDChat; }
    public void setIDChat(int IDChat) { this.IDChat = IDChat; }

    public String getuDNIremitente() { return uDNIremitente; }
    public void setuDNIremitente(String uDNI_remitente) {  this.uDNIremitente = uDNI_remitente; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MensajeId that)) return false;
        return Objects.equals(IDChat, that.IDChat) &&
                Objects.equals(uDNIremitente, that.uDNIremitente) &&
                Objects.equals(fechaHora, that.fechaHora);


    }

    @Override
    public int hashCode() {
        return Objects.hash(IDChat, uDNIremitente, fechaHora);
    }
}
