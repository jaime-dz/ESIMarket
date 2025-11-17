package es.esimarket.backend.mappers;
import es.esimarket.backend.dtos.ChatDTO;
import es.esimarket.backend.entities.Chat;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(target = "nombreProducto", source = "producto.nombre")
    @Mapping(target = "nombre", source = "otroUsuario.nombre")
    @Mapping(target = "apellidos", source = "otroUsuario.apellidos")
    @Mapping(target = "carrera", source = "otroUsuario.carrera")
    ChatDTO toDTO(Chat chat, Usuario otroUsuario, Producto producto);
}
