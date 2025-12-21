package es.esimarket.backend.dtos;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ServicioDTO implements Serializable {

    private int idProd;
    String nombreProd;
    private String DNIcomprador;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fecha;

    private Boolean finalizado;

    ServicioDTO() {}

    public ServicioDTO(int idProd, String nombreProd, String DNIcomprador, LocalDateTime fecha, Boolean finalizado) {
        this.idProd = idProd;
        this.nombreProd = nombreProd;
        this.DNIcomprador = DNIcomprador;
        this.fecha = fecha;
        this.finalizado = finalizado;
    }

    public int getIdProd() {return idProd;}
    public String getNombreProd() {return nombreProd;}
    public String getDNIcomprador() {return DNIcomprador;}
    public LocalDateTime getFecha() {return fecha;}
    public Boolean getFinalizado() {return finalizado;}

    public void setIdProd(int idProd) {this.idProd = idProd;}
    public void setNombreProd(String nombreProd) {this.nombreProd = nombreProd;}
    public void setDNIcomprador(String DNIcomprador) {this.DNIcomprador = DNIcomprador;}
    public void setFecha(LocalDateTime fecha) {this.fecha = fecha;}
    public void setFinalizado(Boolean finalizado) {this.finalizado = finalizado;}

    @Override
    public String toString() {
        return "ServicioDTO{" +
                "idProd=" + idProd +
                ", nombreProd='" + nombreProd + '\'' +
                ", DNIcomprador='" + DNIcomprador + '\'' +
                ", fecha=" + fecha +
                ", finalizado=" + finalizado +
                '}';
    }

}
