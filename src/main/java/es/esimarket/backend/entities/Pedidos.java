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

    @Column(name="IdProd")
    private Integer IdProd;

    @Column(name="EnTaquilla")
    private boolean EnTaquilla;

    @Column(name="NumTaquilla")
    private Integer NTaquilla;

    @Column(name="Estado")
    @Enumerated(EnumType.STRING)
    private Estado Estado;

    public Pedidos() {}

    public Pedidos(Integer IdProd,Estado Estado,boolean enTaquilla)
    {
        this.IdProd = IdProd;
        this.Estado=Estado;
        this.EnTaquilla=enTaquilla;
    }

    public int getIdPedido() {return IdPedido;}
    public void setIdPedido(int IdPedido) {this.IdPedido=IdPedido;}

    public Integer getIdProd() {return IdProd;}
    public void setIdProd(Integer IdCompra) {this.IdProd = IdCompra;}

    public boolean isEnTaquilla() {return EnTaquilla;}
    public void setEnTaquilla(boolean enTaquilla) {EnTaquilla = enTaquilla;}

    public Integer getNTaquilla() {return NTaquilla;}
    public void setNTaquilla(Integer NTaquilla) {this.NTaquilla = NTaquilla;}

    public Estado getEstado() {return Estado;}
    public void setEstado(Estado Estado) {this.Estado=Estado;}

}