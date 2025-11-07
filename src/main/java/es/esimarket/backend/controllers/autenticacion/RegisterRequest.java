package es.esimarket.backend.controllers.autenticacion;
import es.esimarket.backend.entities.Usuario;

public record  RegisterRequest(
        String username,
        String password,
        String name,
        String apellidos,
        String email,
        Usuario.Carrera carrera
) {}
