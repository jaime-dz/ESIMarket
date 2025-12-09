package es.esimarket.backend.mappers;
import es.esimarket.backend.dtos.ChatDTO;
import es.esimarket.backend.entities.Chat;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(target = "id", source = "chat.id")
    @Mapping(target = "nombreProducto", source = "producto.nombre")
    @Mapping(target = "nombreVendedor", source = "otroUsuario.nombre")
    @Mapping(target = "apellidosVendedor", source = "otroUsuario.apellidos")
    @Mapping(target = "carreraVendedor", source = "otroUsuario.carrera")
    ChatDTO toDTO(Chat chat, Usuario otroUsuario, Producto producto);
    Chat toEntity( ChatDTO chat );
}
