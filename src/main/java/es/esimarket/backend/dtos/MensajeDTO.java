package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.Mensaje;

import java.io.Serializable;

public class MensajeDTO implements Serializable {

    private String FechaHora;
    private String texto;

    public MensajeDTO() {}

    public MensajeDTO( Mensaje m){
        this.FechaHora = m.getFechaHora();
        this.texto = m.getTexto();
    }

    public MensajeDTO(String fechaHora, String texto) {
        FechaHora = fechaHora;
        this.texto = texto;
    }

    public String getFechaHora() {return FechaHora;}
    public String getTexto() {return texto;}

    public void setFechaHora(String fechaHora) {FechaHora = fechaHora;}
    public void setTexto(String texto) {this.texto = texto;}

    @Override
    public String toString() {
        return "MensajeDTO{" +
                "FechaHora='" + FechaHora + '\'' +
                ", texto='" + texto + '\'' +
                '}';
    }


}

