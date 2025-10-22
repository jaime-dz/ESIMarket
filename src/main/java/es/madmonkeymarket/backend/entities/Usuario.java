package es.madmonkeymarket.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name="usuarios")
public class Usuario {


    @Id
    @Column(name = "correo", nullable = false)
    private String correo;

    @Column(name = "contrasenna", nullable = false)
    private String contrasenna;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "dni")
    private String dni;

    @Column(name = "saldoMoneda")
    private double saldoMoneda;

    @Column(name = "salt")
    private String salt;


    public Usuario() {}

    public Usuario( String correo, String contrasenna , String nombre, String apellidos, double saldoMoneda, String DNI, String salt)
    {
        this.correo = correo;
	this.contrasenna = contrasenna; 
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.saldoMoneda = saldoMoneda;
        this.dni = DNI;
        this.salt = salt;
    }


}
