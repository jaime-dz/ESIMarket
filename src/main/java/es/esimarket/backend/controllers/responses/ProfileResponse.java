package es.esimarket.backend.controllers.responses;
import es.esimarket.backend.entities.Usuario;

public class ProfileResponse{

    String Nombre;
    String Apellidos;
    String usuario;
    String email;
    Usuario.Carrera carrera;
    long saldo;

    public ProfileResponse(String nombre, String apellidos, String usuario, String email, Usuario.Carrera carrera, long saldo) {
        this.Nombre = nombre;
        this.Apellidos = apellidos;
        this.usuario = usuario;
        this.email = email;
        this.carrera = carrera;
        this.saldo = saldo;
    }

    public String getNombre() {return Nombre;}
    public String getApellidos() {return Apellidos;}
    public String getUsuario() {return usuario;}
    public String getEmail() {return email;}
    public Usuario.Carrera getCarrera() {return carrera;}
    public long getSaldo() {return saldo;}

    public void setNombre(String nombre) {Nombre = nombre;}
    public void setApellidos(String apellidos) {Apellidos = apellidos;}
    public void setUsuario(String usuario) {this.usuario = usuario;}
    public void setEmail(String email) {this.email = email;}
    public void setCarrera(Usuario.Carrera carrera) {this.carrera = carrera;}
    public void setSaldo(long saldo) {this.saldo = saldo;}

    @Override
    public String toString() {
        return "ProfileResponse{" +
                "Nombre='" + Nombre + '\'' +
                ", Apellidos='" + Apellidos + '\'' +
                ", usuario='" + usuario + '\'' +
                ", email='" + email + '\'' +
                ", carrera=" + carrera +
                ", saldo=" + saldo +
                '}';
    }
}


