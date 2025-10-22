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

    @Column(name="Nombre")
    private String Nombre;

    @Column(name="Apellidos")
    private String apellidos;

    @Column(name="Saldo_Monedas")
    private double Saldo_Monedas;

    @Column(name="DNI")
    private String DNI;

    @Column(name="salt")
    private String salt;

    public Usuario() {}

    public Usuario( String correo, String contrasenna , String nombre, String apellidos, double saldo_monedas, String DNI, String salt)
    {
        this.id = new UsuarioId(correo, contrasenna);
        this.Nombre = nombre;
        this.apellidos = apellidos;
        this.Saldo_Monedas = saldo_monedas;
        this.DNI = DNI;
        this.salt = salt;
    }


}
