package es.esimarket.backend.mappers;
import es.esimarket.backend.dtos.UsuarioDTO;
import es.esimarket.backend.entities.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Usuario toEntity(UsuarioDTO usuarioDTO);
    UsuarioDTO toDTO(Usuario usuario);
}
