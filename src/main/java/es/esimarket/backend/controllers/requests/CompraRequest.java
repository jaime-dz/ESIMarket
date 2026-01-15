package es.esimarket.backend.controllers.requests;

import es.esimarket.backend.entities.Producto;

public record CompraRequest (
        Integer idProd,
        Producto.PagoAceptado tipoPago,
        Producto.RecepcionAceptada recepcion,
        Long horas,
        Integer idProdTrueque
) {}
