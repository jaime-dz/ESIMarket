package es.esimarket.backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="Servicios")
public class Servicio {

    @Id
    @Column(name="IdProd",nullable = false)
    private int idProd;

    @Column(name="DNIcomprador")
    private String DNIcomprador;

    @Column(name="Fecha")
    private LocalDateTime fecha;

    @Column(name="Finalizado")
    private Boolean finalizado;

    public Servicio() {}

    public Servicio(int idProd, String DNIcomprador, LocalDateTime fecha, Boolean finalizado) {
        this.idProd = idProd;
        this.DNIcomprador = DNIcomprador;
        this.fecha = fecha;
        this.finalizado = finalizado;
    }

    public int getIdProd() {return idProd;}
    public String getDNIcomprador() {return DNIcomprador;}
    public LocalDateTime getFecha() {return fecha;}
    public Boolean getFinalizado() {return finalizado;}

    public void setIdProd(int idProd) {this.idProd = idProd;}
    public void setDNIcomprador(String DNIcomprador) {this.DNIcomprador = DNIcomprador;}
    public void setFecha(LocalDateTime fecha) {this.fecha = fecha;}
    public void setFinalizado(Boolean finalizado) {this.finalizado = finalizado;}
}


