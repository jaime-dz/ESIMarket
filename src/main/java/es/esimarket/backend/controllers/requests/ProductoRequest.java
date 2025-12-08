package es.esimarket.backend.controllers.requests;

import es.esimarket.backend.entities.Producto;

public record ProductoRequest(
    String uDNIVendedor,
    int precio,
    String descripcion,
    String Nombre,
    String Tipo,
    Producto.estado estado,
    Producto.PagoAceptado pago,
    Producto.RecepcionAceptada recepcionAceptada,
    byte[] foto
) {}