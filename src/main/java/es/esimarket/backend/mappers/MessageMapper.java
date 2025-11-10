package es.esimarket.backend.mappers;
import es.esimarket.backend.dtos.MensajeDTO;
import es.esimarket.backend.entities.Mensaje;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface MessageMapper {
    @Mappings({
            @Mapping(source = "fechaHora", target = "fechaHora"),
            @Mapping(source = "texto", target = "texto")
    })
    MensajeDTO toDto(Mensaje entity);

    @Mappings({
            @Mapping(source = "fechaHora", target = "fechaHora"),
            @Mapping(source = "texto", target = "texto")
    })
    Mensaje toEntity(MensajeDTO dto);
}
