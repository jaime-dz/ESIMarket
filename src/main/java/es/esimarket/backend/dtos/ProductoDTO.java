package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.FotoProd;
import es.esimarket.backend.entities.Producto;
import java.io.Serializable;
import java.util.Arrays;

public class ProductoDTO implements Serializable {

    private int Precio;
    private String Descripcion;
    private String Nombre;
    private String tipo;
    private Producto.estado estado;
    private Producto.RecepcionAceptada recepcionAceptada;
    private byte[] Foto;

    public ProductoDTO() {}

    public ProductoDTO(Producto p , FotoProd fp ){
        this.Precio = p.getPrecio();
        this.Descripcion = p.getDescripcion();
        this.Nombre = p.getNombre();
        this.tipo = p.getTipo();
        this.recepcionAceptada=p.getRecepcionAceptada();
        this.Foto = (fp != null) ? fp.getFoto() : null;
    }

    public ProductoDTO(int precio, String descripcion, String nombre, String tipo, Producto.estado estado,Producto.RecepcionAceptada recepcionAceptada , byte[] foto) {
        this.Precio = precio;
        this.Descripcion = descripcion;
        this.Nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.recepcionAceptada = recepcionAceptada;
        this.Foto = foto;
    }


    public int getPrecio() {return Precio;}
    public String getDescripcion() {return Descripcion;}
    public String getNombre() {return Nombre;}
    public String getTipo() {return tipo;}
    public Producto.estado getEstado() {return estado;}
    public Producto.RecepcionAceptada getRecepcionAceptada() {return recepcionAceptada;}
    public byte[] getFoto() {return Foto;}

    public void setPrecio(int precio) {Precio = precio;}
    public void setDescripcion(String descripcion) {Descripcion = descripcion;}
    public void setNombre(String nombre) {Nombre = nombre;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public void setEstado(Producto.estado estado) {this.estado = estado;}
    public void setRecepcionAceptada(Producto.RecepcionAceptada recepcionAceptada) {this.recepcionAceptada=recepcionAceptada;}
    public void setFoto(byte[] foto) {Foto = foto;}

    @Override
    public String toString() {
        return "ProductoDTO{" +
                "Precio=" + Precio +
                ", Descripcion='" + Descripcion + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", estado=" + estado +
                ", recepcionAceptada=" + recepcionAceptada +
                ", Foto=" + Arrays.toString(Foto) +
                '}';
    }

}
