package es.esimarket.backend.dtos;

import java.io.Serializable;

import es.esimarket.backend.entities.Pedidos;

public class PedidosDTO implements Serializable{

    private int IdPedido;

    private int NTaquilla;

    private boolean EnTaquilla;

    private Pedidos.Estado Estado;

    public PedidosDTO() {}

    public PedidosDTO(int IdPedido, int NTaquilla,boolean EnTaquilla, Pedidos.Estado Estado)
    {
        this.IdPedido = IdPedido;
        this.NTaquilla=NTaquilla;
        this.EnTaquilla=EnTaquilla;
        this.Estado=Estado;
    }

    public int getIdPedido() {return IdPedido;}
    public void setIdPedido(int IdPedido) {this.IdPedido = IdPedido;}

    public int getNTaquilla() {return NTaquilla;}
    public void setIdTaquilla(int NTaquilla) {this.NTaquilla=NTaquilla;}

    public boolean getEnTaquilla() {return EnTaquilla;}
    public void setEnTaquilla(boolean EnTaquilla) {this.EnTaquilla=EnTaquilla;}

    public Pedidos.Estado getEstado() {return Estado;}
    public void setEstado(Pedidos.Estado Estado) {this.Estado=Estado;}

    @Override
    public String toString() {
        return "PedidosDTO{" +
                "NTaquilla=" + NTaquilla +
                ", EnTaquilla='" + EnTaquilla + '\'' +
                ", Estado='" + Estado + '\'' +
                '}';
    }

}