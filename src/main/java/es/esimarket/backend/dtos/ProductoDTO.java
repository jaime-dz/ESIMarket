package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.FotoProd;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Arrays;

public class ProductoDTO implements Serializable {

    private int id;
    private int precio;
    private String descripcion;
    private String nombre;
    private String nombreVendedor;
    private String tipo;
    private Producto.estado estado;
    private Producto.RecepcionAceptada recepcionAceptada;
    private byte[] foto;

    public ProductoDTO() {}

    public ProductoDTO(Producto p , Usuario u, FotoProd fp ) {
        this.id = p.getID();
        this.precio = p.getPrecio();
        this.descripcion = p.getDescripcion();
        this.nombre = p.getNombre();
        this.tipo = p.getTipo();
        this.estado = p.getEstado();
        this.recepcionAceptada=p.getRecepcionAceptada();
        this.foto = (fp != null) ? fp.getFoto() : null;
        this.nombreVendedor = (u != null) ? u.getNombre() : "Vendedor desconocido";
    }

    public ProductoDTO(int id, int precio, String descripcion, String nombre, String tipo, Producto.estado estado,Producto.RecepcionAceptada recepcionAceptada , byte[] foto, String NombreV) {
        this.id = id;
        this.precio = precio;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.recepcionAceptada = recepcionAceptada;
        this.foto = foto;
        this.nombreVendedor = NombreV;
    }

    public String getFotoBase64(){
        if (this.foto != null && this.foto.length > 0) {
            return "data:image/jpeg;base64," + java.util.Base64.getEncoder().encodeToString(this.foto);
        }
        return null;
    }

    public int getId() {return id;}
    public int getPrecio() {return precio;}
    public String getDescripcion() {return descripcion;}
    public String getNombre() {return nombre;}
    public String getNombreVendedor() {return nombreVendedor;}
    public String getTipo() {return tipo;}
    public Producto.estado getEstado() {return estado;}
    public Producto.RecepcionAceptada getRecepcionAceptada() {return recepcionAceptada;}
    public byte[] getFoto() {return foto;}


    public void setId(int id) {this.id = id;}
    public void setPrecio(int precio) {this.precio = precio;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setNombreVendedor(String nombreVendedor) {this.nombreVendedor = nombreVendedor;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public void setEstado(Producto.estado estado) {this.estado = estado;}
    public void setRecepcionAceptada(Producto.RecepcionAceptada recepcionAceptada) {this.recepcionAceptada = recepcionAceptada;}
    public void setFoto(byte[] foto) {this.foto = foto;}

    @Override
    public String toString() {
        return "ProductoDTO{" +
                "id=" + id +
                ", precio=" + precio +
                ", descripcion='" + descripcion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", nombreVendedor='" + nombreVendedor + '\'' +
                ", tipo='" + tipo + '\'' +
                ", estado=" + estado +
                ", recepcionAceptada=" + recepcionAceptada +
                ", foto=" + Arrays.toString(foto) +
                '}';
    }

}
