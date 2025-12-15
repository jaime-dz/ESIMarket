package es.esimarket.backend.controllers.requests;

public record FiltroRequest (
        Long precioSuperior,
        Long precioInferior,
        String nombre,
        String tipo,
        String estado,
        String orden
)
{}
