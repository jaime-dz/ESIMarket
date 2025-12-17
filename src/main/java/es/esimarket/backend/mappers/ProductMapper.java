package es.esimarket.backend.mappers;
import es.esimarket.backend.dtos.ProductoDTO;
import es.esimarket.backend.entities.FotoProd;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.entities.Usuario;
import jakarta.xml.bind.SchemaOutputResolver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Producto toEntity(ProductoDTO productoDTO);

    @Mapping(source = "fp.foto", target = "foto")
    @Mapping(source = "u.nombre", target = "NombreVendedor")
    ProductoDTO toDTO(Producto producto , FotoProd fp, Usuario u);
}
