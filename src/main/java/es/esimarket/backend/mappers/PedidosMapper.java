package es.esimarket.backend.mappers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.esimarket.backend.dtos.PedidosDTO;
import es.esimarket.backend.entities.Pedidos;

@Mapper(componentModel = "spring")
public interface  PedidosMapper{
    PedidosDTO toDto(Pedidos entity);
    Pedidos toEntity(PedidosDTO dto);
}