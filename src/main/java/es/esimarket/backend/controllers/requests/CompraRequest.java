package es.esimarket.backend.controllers.requests;

import es.esimarket.backend.entities.Producto;

public record CompraRequest (
        int idProd,
        Producto.PagoAceptado tipoPago,
        Producto.RecepcionAceptada recepcion,
        Long horas,
        int idProdTrueque
) {}
