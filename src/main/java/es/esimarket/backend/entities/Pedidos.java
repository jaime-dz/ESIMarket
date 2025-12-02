package es.esimarket.backend.entities;

import java.math.BigInteger;

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
    private BigInteger IdCompra;

    @Column(name="NumTaquilla")
    private int NTaquilla;

    @Column(name="Estado")
    private Estado Estado;

    public Pedidos() {}

    public Pedidos(BigInteger IdCompra, int NTaquilla,Estado Estado)
    {
        this.IdCompra = IdCompra;
        this.NTaquilla=NTaquilla;
        this.Estado=Estado;
    }

    public int getIdPedido() {return IdPedido;}
    public void setIdPedido(int IdPedido) {this.IdPedido=IdPedido;}

    public BigInteger getIdCompra() {return IdCompra;}
    public void setIdCompra(BigInteger IdCompra) {this.IdCompra = IdCompra;}

    public int getNTaquilla() {return NTaquilla;}
    public void setIdTaquilla(int NTaquilla) {this.NTaquilla=NTaquilla;}

    public Estado getEstado() {return Estado;}
    public void setEstado(Estado Estado) {this.Estado=Estado;}

}