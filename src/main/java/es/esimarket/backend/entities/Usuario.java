package es.esimarket.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name="usuario")
public class Usuario {


    @Id
    @Column(name = "uDNI", nullable = false)
    private String id;

    @Column(name = "Contrasenna", nullable = false)
    private String contrasenna;

    @Column(name="eMail")
    private String correo;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "Apellidos")
    private String apellidos;

    @Column(name = "Saldo")
    private double saldoMoneda;

    @Column(name = "Salt", nullable = false)
    private byte[] salt;


    public Usuario() {}

    public Usuario( String id, String contrasenna,byte[] salt )
    {
	    this.id = id;
        this.contrasenna = contrasenna;
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

    public byte[] getSalt() { return salt; }
    public void setSalt(byte[] salt) { this.salt = salt; }

}
