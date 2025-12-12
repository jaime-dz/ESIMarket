package es.esimarket.backend.entities;

import java.math.BigInteger;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IdPedido")
    private int IdPedido;

    @Column(name="IdCompra")
    private Integer IdCompra;

    @Column(name="EnTaquilla")
    private boolean EnTaquilla;

    @Column(name="NumTaquilla")
    private Integer NTaquilla;

    @Column(name="Estado")
    @Enumerated(EnumType.STRING)
    private Estado Estado;

    public Pedidos() {}

    public Pedidos(Integer IdCompra,Estado Estado)
    {
        this.IdCompra = IdCompra;
        this.Estado=Estado;
        this.EnTaquilla=false;
    }

    public int getIdPedido() {return IdPedido;}
    public void setIdPedido(int IdPedido) {this.IdPedido=IdPedido;}

    public Integer getIdCompra() {return IdCompra;}
    public void setIdCompra(Integer IdCompra) {this.IdCompra = IdCompra;}

    public boolean isEnTaquilla() {return EnTaquilla;}
    public void setEnTaquilla(boolean enTaquilla) {EnTaquilla = enTaquilla;}

    public int getNTaquilla() {return NTaquilla;}
    public void setNTaquilla(int NTaquilla) {this.NTaquilla = NTaquilla;}

    public Estado getEstado() {return Estado;}
    public void setEstado(Estado Estado) {this.Estado=Estado;}

}