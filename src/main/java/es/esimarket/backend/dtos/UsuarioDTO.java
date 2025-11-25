package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.Usuario;
import java.io.Serializable;

public class UsuarioDTO implements Serializable {

    private String id;
    private String correo;
    private String nombre;
    private String apellidos;
    private long saldoMoneda;
    private Usuario.Carrera carrera;

    public UsuarioDTO(){}

    public UsuarioDTO(Usuario usuario){
        this.id = usuario.getId();
        this.correo = usuario.getCorreo();
        this.nombre = usuario.getNombre();
        this.apellidos = usuario.getApellidos();
        this.saldoMoneda = usuario.getSaldoMoneda();
        this.carrera = usuario.getCarrera();
    }

    public UsuarioDTO(String id, String correo, String nombre, String apellidos, long saldoMoneda ,Usuario.Carrera carrera) {
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.saldoMoneda = saldoMoneda;
        this.carrera = carrera;
    }

    public String getId() {return id;}
    public String getCorreo() {return correo;}
    public String getNombre() {return nombre;}
    public String getApellidos() {return apellidos;}
    public long getSaldoMoneda() {return saldoMoneda;}
    public Usuario.Carrera getCarrera() {return carrera;}

    public void setId(String id) {this.id = id;}
    public void setCorreo(String correo) {this.correo = correo;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setApellidos(String apellidos) {this.apellidos = apellidos;}
    public void setSaldoMoneda(long saldoMoneda) {this.saldoMoneda = saldoMoneda;}
    public void setCarrera(Usuario.Carrera carrera) {this.carrera = carrera;}

    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "id='" + id + '\'' +
                ", correo='" + correo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", saldoMoneda=" + saldoMoneda +
                ", carrera=" + carrera +
                '}';
    }
}
