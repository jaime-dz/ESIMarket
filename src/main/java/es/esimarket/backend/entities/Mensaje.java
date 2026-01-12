package es.esimarket.backend.entities;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private LocalDateTime fechaHora;

    @Column(name= "Texto")
    private String texto;

    public Mensaje(){}

    public Mensaje(int id_chat, String uDNI_rem, LocalDateTime fecha, String texto){
        this.IDChat = id_chat;
        this.uDNIremitente = uDNI_rem;
        this.fechaHora = fecha;
        this.texto = texto;
    }

    public BigInteger getId() {return id;}

    public int getIDChat(){return IDChat;}
    public void setIDChat(int IDChat) { this.IDChat = IDChat; }

    public String getuDNIremitente(){return uDNIremitente;}
    public void setuDNIremitente(String uDNIremitente) { this.uDNIremitente = uDNIremitente; }

    public String getTexto(){return texto;}
    public void setTexto(String texto) {this.texto=texto;}

    public LocalDateTime getFecha(){ return fechaHora;}

    public String getFechaHora(){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return fechaHora.format(formato);
    }

    public String getFechaDia(){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return fechaHora.format(formato);
    }
    
    public String getDia(){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd");
        return fechaHora.format(formato);
    }

    public String getHoraMin(){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");
        return fechaHora.format(formato);
    }
    public void setFechaHora(LocalDateTime FechaHora) { this.fechaHora = FechaHora; }
}
