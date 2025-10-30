package es.esimarket.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="recarga")
public class Recarga{

    @Id
    @Column(name="ID_Transaccion",nullable = false)
    private int ID_Transaccion;

    public Recarga(){}

    public Recarga(int ID_Transaccion)
    {this.ID_Transaccion=ID_Transaccion;}

    public int getID_Transaccion(){return ID_Transaccion;}
    public void setID_Transaccion(int ID_Transaccion){this.ID_Transaccion=ID_Transaccion;}
}