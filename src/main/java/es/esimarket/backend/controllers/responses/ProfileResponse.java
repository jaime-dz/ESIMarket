package es.esimarket.backend.controllers.responses;
import es.esimarket.backend.entities.Usuario;

public record ProfileResponse(
        String Nombre,
        String Apellidos,
        String usuario,
        String email,
        Usuario.Carrera carrera,
        long saldo
)
{}
