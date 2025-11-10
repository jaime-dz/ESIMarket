package es.esimarket.backend.mappers;
import es.esimarket.backend.dtos.DonacionesDTO;
import es.esimarket.backend.entities.Donaciones;
import org.mapstruct.Mapper;

@Mapper
public interface DonationsMapper {
    Donaciones toEntity(DonacionesDTO donacionesDTO);
    DonacionesDTO toDonacionesDTO(Donaciones donaciones);
}
