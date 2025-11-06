package es.esimarket.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="donaciones")
public class Donaciones {

    @Id
    @Column(name="IDUsuario",nullable = false)
    private String IDUsuario;

    @Column(name="num")
    private int Num;

    public Donaciones(){}

    public Donaciones(String IDUsuario,int Num)
    {
        this.IDUsuario = IDUsuario;
        this.Num = Num;
    }

    public String getIDUsuario(){return IDUsuario;}
    public void setIDUsuario(String IDUsuario) {this.IDUsuario = IDUsuario;}

    public int getNum(){return Num;}
    public void setNum(int Num){this.Num = Num;}


}