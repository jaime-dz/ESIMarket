package es.esimarket.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="paquetemonedas")
public class Paquetemonedas{

    @Id
    @Column(name="IDPaquete",nullable = false)
    private int ID_Paquete;

    @Column(name="Nombre", nullable = false)
    private String Nombre;

    @Column(name="PrecioReal", nullable = false)
    private float Precio_Real;

    @Column(name= "MonedasCantidad", nullable = false)
    private int Monedas_Cantidad;

    @Column(name= "Descripcion")
    private String Descripcion;

    @Column(name = "Activo")
    private Boolean Activo;

    public Paquetemonedas(){}

    public Paquetemonedas(int ID_Paquete, String Nombre, float Precio_Real, int Monedas_Cantidad)
    {
        this.ID_Paquete=ID_Paquete;
        this.Nombre=Nombre;
        this.Precio_Real=Precio_Real;
        this.Monedas_Cantidad=Monedas_Cantidad;
    }

    public int getID_Paquete(){return ID_Paquete;}
    public void setID_Paquete(int ID_Paquete) {this.ID_Paquete=ID_Paquete;}

    public String getNombre(){return Nombre;}
    public void setNombre(String Nombre){this.Nombre=Nombre;}

    public float getPrecio_Real(){return Precio_Real;}
    public void setPrecio_Real(float Precio_Real) {this.Precio_Real=Precio_Real;}

    public int getMonedas_Cantidad(){return Monedas_Cantidad;}
    public void setMonedas_Cantidad(int Monedas_Cantidad) {this.Monedas_Cantidad=Monedas_Cantidad;}

    public String getDescripcion(){return Descripcion;}
    public void setDescripcion(String Descripcion){this.Descripcion=Descripcion;}

    public Boolean getActivo(){return Activo;}
    public void setActivo(Boolean Activo) {this.Activo=Activo;}


}