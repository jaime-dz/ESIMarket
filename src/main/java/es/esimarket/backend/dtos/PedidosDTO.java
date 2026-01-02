package es.esimarket.backend.dtos;

import java.io.Serializable;
import java.util.Arrays;

import es.esimarket.backend.entities.Pedidos;

public class PedidosDTO implements Serializable{

    private int idPedido;

    private byte[] foto;

    private String nombreComprador;

    private String nombreVendedor;

    private boolean esComprador;

    private String nombreProd;

    private int nTaquilla;

    private boolean enTaquilla;

    private Pedidos.Estado estado;

    public PedidosDTO() {}

    public PedidosDTO(int IdPedido, byte[] foto, String nombreComprador, String nombreVendedor,boolean esComprador, String nombreProd, int NTaquilla,boolean EnTaquilla, Pedidos.Estado Estado)
    {
        this.idPedido = IdPedido;
        this.foto = foto;
        this.nombreComprador = nombreComprador;
        this.nombreVendedor = nombreVendedor;
        this.esComprador = esComprador;
        this.nombreProd = nombreProd;
        this.nTaquilla =NTaquilla;
        this.enTaquilla =EnTaquilla;
        this.estado =Estado;
    }

    public int getIdPedido() {return idPedido;}
    public void setIdPedido(int IdPedido) {this.idPedido = IdPedido;}

    public String getFotoBase64(){
        if (this.foto != null && this.foto.length > 0) {
            return "data:image/jpeg;base64," + java.util.Base64.getEncoder().encodeToString(this.foto);
        }
        return null;
    }

    public String getNombreComprador() {return nombreComprador;}
    public void setNombreComprador(String nombreComprador) {this.nombreComprador = nombreComprador;}

    public String getNombreVendedor() {return nombreVendedor;}
    public void setNombreVendedor(String nombreVendedor) {this.nombreVendedor = nombreVendedor;}

    public boolean isEsComprador() {return esComprador;}
    public void setEsComprador(boolean esComprador) {this.esComprador = esComprador;}

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
                ", foto=" + Arrays.toString(foto) +
                ", nombreComprador='" + nombreComprador + '\'' +
                ", nombreVendedor='" + nombreVendedor + '\'' +
                ", esComprador=" + esComprador +
                ", nombreProd='" + nombreProd + '\'' +
                ", nTaquilla=" + nTaquilla +
                ", enTaquilla=" + enTaquilla +
                ", estado=" + estado +
                '}';
    }

}