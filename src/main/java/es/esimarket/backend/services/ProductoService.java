package es.esimarket.backend.services;
import es.esimarket.backend.dtos.ProductoDTO;
import es.esimarket.backend.dtos.UsuarioDTO;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.mappers.ProductMapper;
import es.esimarket.backend.repositories.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.esimarket.backend.entities.Producto;

import java.util.*;

import org.springframework.jdbc.core.JdbcTemplate;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private VariosService variosService;


    public ResponseEntity<String> nuevoProducto(String vendedor, int precio, String descripcion, String Nombre, String tipo, Producto.estado estado,Producto.PagoAceptado pa,Producto.RecepcionAceptada recepcionAceptada){

        Producto p = new Producto(vendedor, precio, descripcion, Nombre, tipo, estado,pa,recepcionAceptada);

        productoRepository.save(p);

        return ResponseEntity.ok("Producto registrado correctamente");
    }

    public List<Producto> FiltroProductosGenerico(String json)
    {
        HashMap<String,String> dic = variosService.StringToDictionary(json);


        //tipos contemplados por los que se va a filtrar, Keys del diccionario: PrecioSuperior, PrecioInferior, Nombre, Tipo, Estado, Orden

        String sql = "Select * from Productos where ";

        List<Object> ValoresParametros = new ArrayList<>();

        int ContadorElementos =0;

        for (Map.Entry<String,String> KeyValue : dic.entrySet())
        {
            switch(KeyValue.getKey())
            {
                case "PrecioSuperior":
                    sql = sql + "Precio > ? ";
                    ValoresParametros.add(Integer.parseInt(KeyValue.getValue()));
                    break;
                case "PrecioInferior":
                    sql = sql + "Precio < ? ";
                    ValoresParametros.add(Integer.parseInt(KeyValue.getValue()));
                    break;
                case "Nombre":
                    sql = sql + "Nombre is LIKE '%?%' ";
                    ValoresParametros.add(KeyValue.getValue().toLowerCase());
                    break;
                case "Tipo":
                    sql = sql + "Tipo is LIKE '%?%' ";
                    ValoresParametros.add(KeyValue.getValue().toLowerCase());
                    break;
                case "Estado":
                    sql = sql + "Estado is LIKE '%?' ";
                    ValoresParametros.add(KeyValue.getValue().toLowerCase());
                    break;  
                case "Orden":
                    if(KeyValue.getValue().equals("1")) //Este va a ser ordenarlo ascendente   
                        {
                            sql = sql.substring(0,sql.length()-4);
                            sql = sql + "ORDER BY Precio ASC";
                        }
                    else //Este va a ser ordenarlo por descendente
                        {
                            sql = sql.substring(0,sql.length()-4);
                            sql = sql + "ORDER BY Precio DESC";
                        }     
            }

            ContadorElementos = ContadorElementos +1;

            if(ContadorElementos < dic.size())
                    sql = sql + "AND ";
        }

        return jdbcTemplate.queryForList(sql, Producto.class, ValoresParametros.toArray());

    }

    public List<ProductoDTO> mostrar_productos( List<Producto> productEntities ){

        List<ProductoDTO> productDTOs = new ArrayList<>();

        for( Producto p : productEntities){
            productDTOs.add(productMapper.toDTO(p));
        }

        return productDTOs;
    }
}

