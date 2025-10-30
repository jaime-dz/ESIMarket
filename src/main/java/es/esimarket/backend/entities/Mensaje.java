package es.esimarket.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="mensaje")
public class Mensaje{

    @Id
    @Column(name= "ID_Chat", nullable = false)
    private int ID_Chat;

    @Column(name= "uDNI_remitente", nullable = false)
    private String uDNI_remitente;

    @Column(name= "Texto")
    private String Texto;

    @Column(name = "FechaHora", nullable = false)
    private String FechaHora;

    public Mensaje(){}

    public Mensaje(int ID_Chat, String uDNI_remitente, String FechaHora)
    {
        this.ID_Chat=ID_Chat;
        this.uDNI_remitente=uDNI_remitente;
        this.FechaHora=FechaHora;

    }

    public int getID_Chat(){return ID_Chat;}
    public void setID_Chat(int ID_Chat) {this.ID_Chat=ID_Chat;}

    public String getuDNI_remitente(){return uDNI_remitente;}
    public void setuDNI_remitente(String uDNI_remitente) {this.uDNI_remitente=uDNI_remitente;}

    public String getTexto(){return Texto;}
    public void setTexto(String Texto) {this.Texto=Texto;}

    public String getFechaHora(){return FechaHora;}
    public void setFechaHora(String FechaHora) {this.FechaHora=FechaHora;}
}
