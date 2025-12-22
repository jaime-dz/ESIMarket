package es.esimarket.backend.controllers.requests;

import es.esimarket.backend.entities.Producto;

public record ProductoRequest(
    int precio,
    String descripcion,
    String nombre,
    String tipo,
    Producto.estado estado,
    Producto.PagoAceptado pago,
    Producto.RecepcionAceptada recepcionAceptada,
    byte[] foto
) {}