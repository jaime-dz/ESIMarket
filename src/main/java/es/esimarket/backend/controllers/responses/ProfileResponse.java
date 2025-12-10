package es.esimarket.backend.controllers.responses;
import es.esimarket.backend.entities.Usuario;

public class ProfileResponse{

    String nombre;
    String apellidos;
    String usuario;
    String email;
    Usuario.Carrera carrera;
    Long saldo;

    public ProfileResponse() {}

    public ProfileResponse(String nombre, String apellidos, String usuario, String email, Usuario.Carrera carrera, Long saldo) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.usuario = usuario;
        this.email = email;
        this.carrera = carrera;
        this.saldo = saldo;
    }

    public String getNombre() {return nombre;}
    public String getApellidos() {return apellidos;}
    public String getUsuario() {return usuario;}
    public String getEmail() {return email;}
    public Usuario.Carrera getCarrera() {return carrera;}
    public Long getSaldo() {return saldo;}

    public void setNombre(String nombre) {nombre = nombre;}
    public void setApellidos(String apellidos) {apellidos = apellidos;}
    public void setUsuario(String usuario) {this.usuario = usuario;}
    public void setEmail(String email) {this.email = email;}
    public void setCarrera(Usuario.Carrera carrera) {this.carrera = carrera;}
    public void setSaldo(Long saldo) {this.saldo = saldo;}

    @Override
    public String toString() {
        return "ProfileResponse{" +
                "Nombre='" + nombre + '\'' +
                ", Apellidos='" + apellidos + '\'' +
                ", usuario='" + usuario + '\'' +
                ", email='" + email + '\'' +
                ", carrera=" + carrera +
                ", saldo=" + saldo +
                '}';
    }
}


