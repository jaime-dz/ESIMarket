package es.esimarket.backend.mappers;
import es.esimarket.backend.dtos.ProductoDTO;
import es.esimarket.backend.entities.Producto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Producto toEntity(ProductoDTO productoDTO);
    ProductoDTO toDTO(Producto producto);
}
