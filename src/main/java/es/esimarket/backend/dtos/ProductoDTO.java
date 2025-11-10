package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.Producto;
import java.io.Serializable;

public class ProductoDTO implements Serializable {

    private int Precio;
    private String Descripcion;
    private String Nombre;
    private String tipo;

    public ProductoDTO() {}

    public ProductoDTO( Producto p ){
        this.Precio = p.getPrecio();
        this.Descripcion = p.getDescripcion();
        this.Nombre = p.getNombre();
        this.tipo = p.getTipo();
    }

    public ProductoDTO(int precio, String descripcion, String nombre, String tipo) {
        Precio = precio;
        Descripcion = descripcion;
        Nombre = nombre;
        this.tipo = tipo;
    }


    public int getPrecio() {return Precio;}
    public String getDescripcion() {return Descripcion;}
    public String getNombre() {return Nombre;}
    public String getTipo() {return tipo;}

    public void setPrecio(int precio) {Precio = precio;}
    public void setDescripcion(String descripcion) {Descripcion = descripcion;}
    public void setNombre(String nombre) {Nombre = nombre;}
    public void setTipo(String tipo) {this.tipo = tipo;}

    @Override
    public String toString() {
        return "ProductoDTO{" +
                "Precio=" + Precio +
                ", Descripcion='" + Descripcion + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
