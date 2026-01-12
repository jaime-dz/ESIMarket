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
    @Mapping(source = "u.nombre", target = "nombreVendedor")
    @Mapping(source = "producto.nombre", target = "nombre")
    @Mapping(source = "producto.precio", target = "precio")
    @Mapping(source = "producto.descripcion", target = "descripcion")
    @Mapping(source = "producto.tipo", target = "tipo")
    @Mapping(source = "producto.estado", target = "estado")
    @Mapping(source = "producto.ID", target = "id")
    ProductoDTO toDTO(Producto producto , FotoProd fp, Usuario u);
}
