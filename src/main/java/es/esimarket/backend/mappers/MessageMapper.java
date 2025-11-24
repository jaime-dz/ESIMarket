package es.esimarket.backend.mappers;
import es.esimarket.backend.dtos.MensajeDTO;
import es.esimarket.backend.entities.Mensaje;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MensajeDTO toDto(Mensaje entity);
    Mensaje toEntity(MensajeDTO dto);
}
