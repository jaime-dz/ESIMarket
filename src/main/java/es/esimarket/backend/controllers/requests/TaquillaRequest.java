package es.esimarket.backend.controllers.requests;

import es.esimarket.backend.entities.Producto;

public record TaquillaRequest(
        int idPedido,
        int taquilla
) {}
