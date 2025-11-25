package es.esimarket.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jdk.jfr.Timestamp;

@Entity
@Table(name="pedido")
public class Pedidos{

    public enum Estado{
        PorEntregar,
        Entregado,
        Recogido
    }

    @Id
    @Column(name="IdPedido")
    private int IdPedido;

    @Column(name="IdCompra")
    private int IdCompra;

    @Column(name="NumTaquilla")
    private int NTaquilla;

    @Column(name="EnTaquilla")
    private boolean EnTaquilla;

    @Column(name="Estado")
    private Estado Estado;

    public Pedidos() {}

    public Pedidos(int IdPedido,int IdCompra, int NTaquilla, boolean EnTaquilla,Estado Estado)
    {
        this.IdPedido=IdPedido;
        this.IdCompra=IdCompra;
        this.NTaquilla=NTaquilla;
        this.EnTaquilla=EnTaquilla;
        this.Estado=Estado;
    }

    public int getIdPedido() {return IdPedido;}
    public void setIdPedido(int IdPedido) {this.IdPedido=IdPedido;}

    public int getIdCompra() {return IdCompra;}
    public void setIdCompra(int IdCompra) {this.IdCompra=IdCompra;}

    public int getNTaquilla() {return NTaquilla;}
    public void setIdTaquilla(int NTaquilla) {this.NTaquilla=NTaquilla;}

    public boolean getEnTaquilla() {return EnTaquilla;}
    public void setEnTaquilla(boolean EnTaquilla) {this.EnTaquilla=EnTaquilla;}

    public Estado getEstado() {return Estado;}
    public void setEstado(Estado Estado) {this.Estado=Estado;}

}