package es.esimarket.backend.entities;
import jakarta.persistence.*;
import org.hibernate.sql.ast.tree.from.CorrelatedPluralTableGroup;

@Entity
@Table(name = "producto")
public class Producto{

    public enum estado{
        Precintado,
        Nuevo,
        Casi_Nuevo,
        Correcto,
        Desgastado
    }

    public enum PagoAceptado{
        Monedas,
        Trueque,
        Ambos
    }

    public enum RecepcionAceptada{
        EnMano,
        Taquilla
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID",nullable = false)
    private int ID;

    @Column(name="uDNIVendedor")
    private String uDNI_Vendedor;

    @Column(name="Precio")
    private int Precio;

    @Column(name="Descripcion")
    private String Descripcion;

    @Column(name="Nombre")
    private String Nombre;

    @Column(name="Tipo")
    private String tipo;

    @Column(name="Estado")
    private estado estado;

    @Column(name="pagoAceptado")
    private PagoAceptado pagoAceptado;

    @Column(name="recepcionAceptada")
    private RecepcionAceptada recepcionAceptada;

    public Producto(){}

    public Producto( String uDNI_Vendedor, int precio, String descripcion, String nombre, String tipo, estado estado, PagoAceptado pagoAceptado,RecepcionAceptada recepcionAceptada) {
        this.uDNI_Vendedor = uDNI_Vendedor;
        this.Precio = precio;
        this.Descripcion = descripcion;
        this.Nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.pagoAceptado = pagoAceptado;
        this.recepcionAceptada=recepcionAceptada;
    }

    public int getID(){return ID;}
    public void setID(int ID){this.ID=ID;}

    public String getuDNI_Vendedor(){return uDNI_Vendedor;}
    public void setuDNI_Vendedor(String uDNI_Vendedor){this.uDNI_Vendedor=uDNI_Vendedor;}

    public int getPrecio(){return Precio;}
    public void setPrecio(int Precio){this.Precio=Precio;}

    public String getDescripcion(){return Descripcion;}
    public void setDescripcion(String Descripcion){this.Descripcion=Descripcion;}

    public String getNombre(){return Nombre;}
    public void setNombre(String Nombre){this.Nombre=Nombre;}

    public String getTipo(){return tipo;}
    public void setTipo(String tipo){this.tipo=tipo;}

    public estado getEstado() {return estado;}
    public void setEstado(estado estado) {this.estado = estado;}

    public PagoAceptado getPagoAceptado() {return pagoAceptado;}
    public void setPagoAceptado(PagoAceptado pagoAceptado) {this.pagoAceptado = pagoAceptado;}

    public RecepcionAceptada getRecepcionAceptada() {return recepcionAceptada;}
    public void setRecepcionAceptada(RecepcionAceptada recepcionAceptada) {this.recepcionAceptada=recepcionAceptada;}


}