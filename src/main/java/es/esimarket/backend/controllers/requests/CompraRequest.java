package es.esimarket.backend.controllers.requests;

import es.esimarket.backend.entities.Producto;

public record CompraRequest (
        int idProd,
        int idProdOfrecido,
        Producto.PagoAceptado tipoPago,
        Producto.RecepcionAceptada recepcion,
        int taquilla,
        int idProdTrueque
)
{}
