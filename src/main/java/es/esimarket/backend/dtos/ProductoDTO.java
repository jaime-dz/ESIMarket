package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.Producto;
import java.io.Serializable;

public class ProductoDTO implements Serializable {

    private int Precio;
    private String Descripcion;
    private String Nombre;
    private String tipo;
    private Producto.estado estado;
    private Producto.RecepcionAceptada recepcionAceptada;

    public ProductoDTO() {}

    public ProductoDTO( Producto p ){
        this.Precio = p.getPrecio();
        this.Descripcion = p.getDescripcion();
        this.Nombre = p.getNombre();
        this.tipo = p.getTipo();
        this.recepcionAceptada=p.getRecepcionAceptada();
    }

    public ProductoDTO(int precio, String descripcion, String nombre, String tipo, Producto.estado estado,Producto.RecepcionAceptada recepcionAceptada) {
        this.Precio = precio;
        this.Descripcion = descripcion;
        this.Nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.recepcionAceptada = recepcionAceptada;
    }


    public int getPrecio() {return Precio;}
    public String getDescripcion() {return Descripcion;}
    public String getNombre() {return Nombre;}
    public String getTipo() {return tipo;}
    public Producto.estado getEstado() {return estado;}
    public Producto.RecepcionAceptada getRecepcionAceptada() {return recepcionAceptada;}

    public void setPrecio(int precio) {Precio = precio;}
    public void setDescripcion(String descripcion) {Descripcion = descripcion;}
    public void setNombre(String nombre) {Nombre = nombre;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public void setEstado(Producto.estado estado) {this.estado = estado;}
    public void setRecepcionAceptada(Producto.RecepcionAceptada recepcionAceptada) {this.recepcionAceptada=recepcionAceptada;}

    @Override
    public String toString() {
        return "ProductoDTO{" +
                "Precio=" + Precio +
                ", Descripcion='" + Descripcion + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", RecepcionAceptada='" + recepcionAceptada + '\'' +
                '}';
    }
}
