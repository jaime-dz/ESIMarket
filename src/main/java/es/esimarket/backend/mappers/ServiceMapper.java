package es.esimarket.backend.mappers;
import es.esimarket.backend.dtos.ServicioDTO;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.entities.Servicio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    Servicio toEntity(ServicioDTO s );

    @Mapping(source = "p.nombre", target = "nombreProd")
    ServicioDTO toDTO(Servicio s , Producto p);
}
