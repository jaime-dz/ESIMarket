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


