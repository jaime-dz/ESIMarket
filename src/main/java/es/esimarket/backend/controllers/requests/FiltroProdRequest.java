package es.esimarket.backend.controllers.requests;

public record FiltroProdRequest (
        Long precioSuperior,
        Long precioInferior,
        String nombre,
        String tipo,
        String estado,
        String orden
)
{}
