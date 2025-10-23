package es.madmonkeymarket.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name="usuarios")
public class Usuario {


    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "contrasenna", nullable = false)
    private String contrasenna;

    @Column(name="correo" , nullable = false)
    private String correo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "saldoMoneda")
    private double saldoMoneda;

    @Column(name = "salt")
    private String salt;


    public Usuario() {}

    public Usuario( String id, String correo, String contrasenna , String nombre, String apellidos, double saldoMoneda, String salt)
    {
	this.id = id;
        this.correo = correo;
	this.contrasenna = contrasenna;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.saldoMoneda = saldoMoneda;
        this.salt = salt;
    }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasenna() { return contrasenna; }
    public void setContrasenna(String contrasenna) { this.contrasenna = contrasenna; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Double getSaldoMoneda() { return saldoMoneda; }
    public void setSaldoMoneda(Double saldoMoneda) { this.saldoMoneda = saldoMoneda; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

}
