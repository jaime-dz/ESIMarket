package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.Mensaje;

import java.io.Serializable;

public class MensajeDTO implements Serializable {

    private String FechaHora;
    private String uDNIremitente;
    private String texto;

    public MensajeDTO() {}

    public MensajeDTO( Mensaje m){
        this.FechaHora = m.getFechaHora();
        this.texto = m.getTexto();
        this.uDNIremitente = m.getuDNIremitente();
    }

    public MensajeDTO(String fechaHora, String texto, String uDNIremitente) {
        this.FechaHora = fechaHora;
        this.texto = texto;
        this.uDNIremitente = uDNIremitente;
    }

    public String getFechaHora() {return FechaHora;}
    public String getuDNIremitente() {return uDNIremitente;}
    public String getTexto() {return texto;}

    public void setFechaHora(String fechaHora) {FechaHora = fechaHora;}
    public void setuDNIremitente(String uDNIremitente) {this.uDNIremitente = uDNIremitente;}
    public void setTexto(String texto) {this.texto = texto;}

    @Override
    public String toString() {
        return "MensajeDTO{" +
                "FechaHora='" + FechaHora + '\'' +
                ", uDNIremitente='" + uDNIremitente + '\'' +
                ", texto='" + texto + '\'' +
                '}';
    }
}

