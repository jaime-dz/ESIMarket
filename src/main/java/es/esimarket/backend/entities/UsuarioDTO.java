package es.esimarket.backend.entities;

import java.io.Serializable;

public class UsuarioDTO implements Serializable {

    private final String id;
    private final String correo;
    private final String nombre;
    private final String apellidos;
    private final double saldoMoneda;
    private final Usuario.Carrera carrera;

    UsuarioDTO(String id, String correo, String nombre, String apellidos, double saldoMoneda ,Usuario.Carrera carrera) {
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.saldoMoneda = saldoMoneda;
        this.carrera = carrera;
    }

    public String getId() { return id; }
    public String getCorreo() { return correo; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public double getSaldoMoneda() { return saldoMoneda; }
    public Usuario.Carrera getCarrera() { return carrera; }

}
