package es.esimarket.backend.entities;
import jakarta.persistence.*;

import java.math.BigInteger;

@Entity
@Table(name="mensaje")
public class Mensaje{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IdMensaje")
    private BigInteger id;

    @Column(name= "IDChat", nullable = false)
    private int IDChat;

    @Column(name= "uDNIremitente", nullable = false)
    private String uDNIremitente;

    @Column(name = "FechaHora", nullable = false)
    private String fechaHora;

    @Column(name= "Texto")
    private String texto;

    public Mensaje(){}

    public Mensaje(int id_chat, String uDNI_rem, String fecha, String texto){
        this.IDChat = id_chat;
        this.uDNIremitente = uDNI_rem;
        this.fechaHora = fecha;
        this.texto = texto;
    }

    public int getIDChat(){return IDChat;}
    public void setIDChat(int IDChat) { this.IDChat = IDChat; }

    public String getuDNIremitente(){return uDNIremitente;}
    public void setuDNIremitente(String uDNIremitente) { this.uDNIremitente = uDNIremitente; }

    public String getTexto(){return texto;}
    public void setTexto(String texto) {this.texto=texto;}

    public String getFechaHora(){return fechaHora;}
    public void setFechaHora(String FechaHora) { this.fechaHora = FechaHora; }
}
