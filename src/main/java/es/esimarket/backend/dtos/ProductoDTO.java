package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.FotoProd;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Arrays;

public class ProductoDTO implements Serializable {

    private int id;
    private Integer precio;
    private String descripcion;
    private String nombre;
    private String uDNIVendedor;
    private String nombreVendedor;
    private String tipo;
    private Producto.estado estado;
    private Producto.PagoAceptado pago;
    private Producto.RecepcionAceptada recepcionAceptada;
    private byte[] foto;
    boolean isDisponible;

    public ProductoDTO() {}

    public ProductoDTO(Producto p , Usuario u, FotoProd fp ) {
        this.id = p.getID();
        this.precio = (p.getPrecio() == null) ? null : p.getPrecio() ;
        this.descripcion = p.getDescripcion();
        this.nombre = p.getNombre();
        this.tipo = p.getTipo();
        this.estado = p.getEstado();
        this.pago = p.getPagoAceptado();
        this.recepcionAceptada=p.getRecepcionAceptada();
        this.foto = (fp != null) ? fp.getFoto() : null;
        this.nombreVendedor = (u != null) ? u.getNombre() : "Vendedor desconocido";
        this.uDNIVendedor = (u != null) ? u.getId() : null;
        this.isDisponible = p.isDisponible();
    }

    public ProductoDTO(int id, int precio, String descripcion, String nombre, String tipo,Producto.PagoAceptado pago ,Producto.estado estado,Producto.RecepcionAceptada recepcionAceptada , byte[] foto, String NombreV) {
        this.id = id;
        this.precio = precio;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.pago = pago;
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
    public Integer getPrecio() {return precio;}
    public String getDescripcion() {return descripcion;}
    public String getNombre() {return nombre;}
    public String getuDNIVendedor() {return uDNIVendedor;}
    public String getNombreVendedor() {return nombreVendedor;}
    public String getTipo() {return tipo;}
    public Producto.estado getEstado() {return estado;}
    public Producto.PagoAceptado getPago() {return pago;}
    public Producto.RecepcionAceptada getRecepcionAceptada() {return recepcionAceptada;}
    public byte[] getFoto() {return foto;}
    public boolean isDisponible() {return isDisponible;}

    public void setId(int id) {this.id = id;}
    public void setPrecio(Integer precio) {this.precio = precio;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setuDNIVendedor(String uDNIVendedor) {this.uDNIVendedor = uDNIVendedor;}
    public void setNombreVendedor(String nombreVendedor) {this.nombreVendedor = nombreVendedor;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public void setEstado(Producto.estado estado) {this.estado = estado;}
    public void setPago(Producto.PagoAceptado pago) {this.pago = pago;}
    public void setRecepcionAceptada(Producto.RecepcionAceptada recepcionAceptada) {this.recepcionAceptada = recepcionAceptada;}
    public void setFoto(byte[] foto) {this.foto = foto;}
    public void setDisponible(boolean disponible) {isDisponible = disponible;}

    @Override
    public String toString() {
        return "ProductoDTO{" +
                "id=" + id +
                ", precio=" + precio +
                ", descripcion='" + descripcion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", uDNIVendedor='" + uDNIVendedor + '\'' +
                ", nombreVendedor='" + nombreVendedor + '\'' +
                ", tipo='" + tipo + '\'' +
                ", estado=" + estado +
                ", pago=" + pago +
                ", recepcionAceptada=" + recepcionAceptada +
                ", foto=" + Arrays.toString(foto) +
                ", isDisponible=" + isDisponible +
                '}';
    }
}
