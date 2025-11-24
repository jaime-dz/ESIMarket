package es.esimarket.backend.mappers;
import es.esimarket.backend.dtos.DonacionesDTO;
import es.esimarket.backend.entities.Donaciones;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DonationsMapper {
    Donaciones toEntity(DonacionesDTO donacionesDTO);
    DonacionesDTO toDonacionesDTO(Donaciones donaciones);
}
