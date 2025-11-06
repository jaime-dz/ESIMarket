package es.esimarket.backend.controllers.autenticacion;

public record   RegisterRequest(
        String username,
        String password,
        String name,
        String apellidos,
        String email
) {}
