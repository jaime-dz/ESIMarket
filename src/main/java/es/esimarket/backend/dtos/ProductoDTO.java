package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.FotoProd;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Arrays;

public class ProductoDTO implements Serializable {

    private int ID;
    private int Precio;
    private String Descripcion;
    private String Nombre;
    private String NombreVendedor;
    private String tipo;
    private Producto.estado estado;
    private Producto.RecepcionAceptada recepcionAceptada;
    private byte[] Foto;

    public ProductoDTO() {}

    public ProductoDTO(Producto p , Usuario u, FotoProd fp ) {
        this.ID = p.getID();
        this.Precio = p.getPrecio();
        this.Descripcion = p.getDescripcion();
        this.Nombre = p.getNombre();
        this.tipo = p.getTipo();
        this.recepcionAceptada=p.getRecepcionAceptada();
        this.Foto = (fp != null) ? fp.getFoto() : null;
        this.NombreVendedor = u.getNombre();
    }

    public ProductoDTO(int id, int precio, String descripcion, String nombre, String tipo, Producto.estado estado,Producto.RecepcionAceptada recepcionAceptada , byte[] foto, String NombreV) {
        this.ID = id;
        this.Precio = precio;
        this.Descripcion = descripcion;
        this.Nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.recepcionAceptada = recepcionAceptada;
        this.Foto = foto;
        this.NombreVendedor = NombreV;
    }

    public int getID() {return ID;}
    public int getPrecio() {return Precio;}
    public String getDescripcion() {return Descripcion;}
    public String getNombre() {return Nombre;}
    public String getTipo() {return tipo;}
    public Producto.estado getEstado() {return estado;}
    public Producto.RecepcionAceptada getRecepcionAceptada() {return recepcionAceptada;}
    public byte[] getFoto() {return Foto;}
    public String getNombreVendedor() {return NombreVendedor;}

    public void setID(int ID) {this.ID = ID;}
    public void setPrecio(int precio) {Precio = precio;}
    public void setDescripcion(String descripcion) {Descripcion = descripcion;}
    public void setNombre(String nombre) {Nombre = nombre;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public void setEstado(Producto.estado estado) {this.estado = estado;}
    public void setRecepcionAceptada(Producto.RecepcionAceptada recepcionAceptada) {this.recepcionAceptada=recepcionAceptada;}
    public void setFoto(byte[] foto) {Foto = foto;}
    public void setNombreVendedor(String nombreVendedor) {NombreVendedor = nombreVendedor;}

    @Override
    public String toString() {
        return "ProductoDTO{" +
                "Id=" + ID +
                ", Precio=" + Precio +
                ", Descripcion='" + Descripcion + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", NombreVendedor='" + NombreVendedor + '\'' +
                ", tipo='" + tipo + '\'' +
                ", estado=" + estado +
                ", recepcionAceptada=" + recepcionAceptada +
                ", Foto=" + java.util.Base64.getEncoder().encodeToString(Foto) +
                '}';
    }
}
