package es.madmonkeymarket.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name="usuarios")
public class Usuario {

    @EmbeddedId
    private UsuarioId id;

    @Column(name="nombre")
    private String nombre;

    @Column(name="apellidos")
    private String apellidos;

    @Column(name="saldo_monedas")
    private double saldo_monedas;

    @Column(name="dni")
    private String dni;

    @Column(name="salt")
    private String salt;

    public Usuario() {}

    public Usuario( String correo, String contrasenna , String nombre, String apellidos, double saldo_monedas, String DNI, String salt)
    {
        this.id = new UsuarioId(correo, contrasenna);
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.saldo_monedas = saldo_monedas;
        this.dni = DNI;
        this.salt = salt;
    }


}
