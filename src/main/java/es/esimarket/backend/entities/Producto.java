package es.esimarket.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "producto")
public class Producto{

    @Id
    @Column(name="ID",nullable = false)
    private int ID;

    @Column(name="uDNI_Vendedor")
    private String uDNI_Vendedor;

    @Column(name="Precio")
    private int Precio;

    @Column(name="Descripcion")
    private String Descripcion;

    @Column(name="Nombre")
    private String Nombre;

    @Column(name="Tipo")
    private String Tipo;

    public Producto(){}

    public Producto(int ID)
    {this.ID=ID;}

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

    public String getTipo(){return Tipo;}
    public void setTipo(String Tipo){this.Tipo=Tipo;}

}