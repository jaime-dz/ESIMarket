package es.esimarket.backend.dtos;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ServicioDTO implements Serializable {

    private int idProd;
    String nombreProd;
    private String nombreComprador;
    private String nombreVendedor;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime fecha;

    private Boolean finalizado;

    ServicioDTO() {}

    public ServicioDTO(int idProd, String nombreProd, String nombreComprador, String nombreVendedor, LocalDateTime fecha, Boolean finalizado) {
        this.idProd = idProd;
        this.nombreProd = nombreProd;
        this.nombreComprador = nombreComprador;
        this.nombreVendedor = nombreVendedor;
        this.fecha = fecha;
        this.finalizado = finalizado;
    }

    public int getIdProd() {return idProd;}
    public String getNombreProd() {return nombreProd;}
    public String getNombreComprador() {return nombreComprador;}
    public String getNombreVendedor() {return nombreVendedor;}
    public LocalDateTime getFecha() {return fecha;}
    public Boolean getFinalizado() {return finalizado;}

    public void setIdProd(int idProd) {this.idProd = idProd;}
    public void setNombreProd(String nombreProd) {this.nombreProd = nombreProd;}
    public void setNombreComprador(String nombreComprador) {this.nombreComprador = nombreComprador;}
    public void setNombreVendedor(String nombreVendedor) {this.nombreVendedor = nombreVendedor;}
    public void setFecha(LocalDateTime fecha) {this.fecha = fecha;}
    public void setFinalizado(Boolean finalizado) {this.finalizado = finalizado;}

    @Override
    public String toString() {
        return "ServicioDTO{" +
                "idProd=" + idProd +
                ", nombreProd='" + nombreProd + '\'' +
                ", nombreComprador='" + nombreComprador + '\'' +
                ", nombreVendedor='" + nombreVendedor + '\'' +
                ", fecha=" + fecha +
                ", finalizado=" + finalizado +
                '}';
    }

}
