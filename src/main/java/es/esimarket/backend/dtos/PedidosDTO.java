package es.esimarket.backend.dtos;

import java.io.Serializable;

import es.esimarket.backend.entities.Pedidos;

public class PedidosDTO implements Serializable{

    private int idPedido;

    private String nombreComprador;

    private String nombreProd;

    private int nTaquilla;

    private boolean enTaquilla;

    private Pedidos.Estado estado;

    public PedidosDTO() {}

    public PedidosDTO(int IdPedido, String nombreComprador, String nombreProd, int NTaquilla,boolean EnTaquilla, Pedidos.Estado Estado)
    {
        this.idPedido = IdPedido;
        this.nombreComprador = nombreComprador;
        this.nombreProd = nombreProd;
        this.nTaquilla =NTaquilla;
        this.enTaquilla =EnTaquilla;
        this.estado =Estado;
    }

    public int getIdPedido() {return idPedido;}
    public void setIdPedido(int IdPedido) {this.idPedido = IdPedido;}

    public String getNombreComprador() {return nombreComprador;}
    public void setNombreComprador(String nombreComprador) {this.nombreComprador = nombreComprador;}

    public String getNombreProd() {return nombreProd;}
    public void setNombreProd(String nombreProd) {this.nombreProd = nombreProd;}

    public boolean isEnTaquilla() {return enTaquilla;}
    public void setEnTaquilla(boolean enTaquilla) {this.enTaquilla = enTaquilla;}

    public int getnTaquilla() {return nTaquilla;}
    public void setIdTaquilla(int NTaquilla) {this.nTaquilla =NTaquilla;}

    public Pedidos.Estado getEstado() {return estado;}
    public void setEstado(Pedidos.Estado Estado) {this.estado =Estado;}

    @Override
    public String toString() {
        return "PedidosDTO{" +
                "idPedido=" + idPedido +
                ", nombreComprador='" + nombreComprador + '\'' +
                ", nombreProd='" + nombreProd + '\'' +
                ", nTaquilla=" + nTaquilla +
                ", enTaquilla=" + enTaquilla +
                ", estado=" + estado +
                '}';
    }
}