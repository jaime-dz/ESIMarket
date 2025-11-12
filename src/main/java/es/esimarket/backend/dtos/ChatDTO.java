package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.Usuario;

import java.io.Serializable;

public class ChatDTO implements Serializable {

    private String nombreProducto;
    private String nombre;
    private String apellidos;
    private Usuario.Carrera carrera;

    public ChatDTO(String nombreProducto, String nombre, String apellidos, Usuario.Carrera carrera) {
        this.nombreProducto = nombreProducto;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.carrera = carrera;
    }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public Usuario.Carrera getCarrera() { return carrera; }
    public void setCarrera(Usuario.Carrera carrera) { this.carrera = carrera; }

    @Override
    public String toString() {
        return "ChatDTO{" +
                "nombreProducto='" + nombreProducto + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", carrera=" + carrera +
                '}';
    }
}
