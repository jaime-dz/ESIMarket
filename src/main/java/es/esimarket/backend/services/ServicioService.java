package es.esimarket.backend.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import es.esimarket.backend.dtos.ServicioDTO;
import es.esimarket.backend.entities.*;
import es.esimarket.backend.exceptions.CannotCompleteActionError;
import es.esimarket.backend.exceptions.CannotCreateProductError;
import es.esimarket.backend.exceptions.CannotCreateUserError;
import es.esimarket.backend.mappers.ServiceMapper;
import es.esimarket.backend.repositories.CompraRepository;
import es.esimarket.backend.repositories.ProductoRepository;
import es.esimarket.backend.repositories.UsuarioRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;

import es.esimarket.backend.repositories.ServicioRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioService{

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private VariosService variosService;

    public void CrearServicioPendiente(int idProd, String DNIcomprador)
    {
        Servicio s = new Servicio(idProd,DNIcomprador,null,false);

        servicioRepository.save(s);
    }

    public String modificarFecha(int idProd, String DNIcomprador,String fechaString)
    {
        Servicio s = servicioRepository.findByidProdAndDNIcomprador(idProd, DNIcomprador);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");
        LocalDateTime fecha = LocalDateTime.parse(fechaString, formatter);

        s.setFecha(fecha);

        servicioRepository.save(s);

        return "Se ha modificado la fecha";
    }

    @Transactional
    public String finalizarServicio(int idProd, String dniSolicitante)
    {
        Producto p = productoRepository.findByID(idProd);

        if (!p.getuDNI_Vendedor().equals(dniSolicitante))
            throw new CannotCompleteActionError("No eres el propietario de este producto");

        Servicio s = servicioRepository.findByIdProd(idProd);

        if (s == null) throw new CannotCompleteActionError("Servicio no encontrado");

        s.setFinalizado(true);
        servicioRepository.save(s);

        return "Se ha finalizado el servicio";
    }

    public List<ServicioDTO> mostrar_servicios_usuario(String DNI)
    {

        StringBuilder sql = new StringBuilder("SELECT s.* FROM servicios s JOIN producto p ON s.IdProd = p.ID WHERE ( s.DNIcomprador = ? OR p.uDNIVendedor = ? ) AND s.Finalizado = 0 ");
        List<Object> params = new ArrayList<>();

        params.add(DNI);
        params.add(DNI);

        List<Servicio> services = jdbcTemplate.query(String.valueOf(sql), new BeanPropertyRowMapper<>(Servicio.class), params.toArray());

        List<ServicioDTO> DTOservices = new ArrayList<>();

        for( Servicio s : services){

            Producto p = productoRepository.findById(s.getIdProd()).orElseThrow(()-> new CannotCreateProductError("El producto no existe"));
            Compra c = compraRepository.findByIDProducto(s.getIdProd());
            Usuario uC = usuarioRepository.findById(c.getuDNIComprador()).orElseThrow(()->new CannotCreateUserError("Usuario no encontrado"));
            Usuario uV = usuarioRepository.findById(p.getuDNI_Vendedor()).orElseThrow(()->new CannotCreateUserError("Usuario no encontrado"));

            DTOservices.add(new ServicioDTO(s.getIdProd(),p.getNombre(),(c.getuDNIComprador().equals(DNI)) ? null : uC.getNombre(),(p.getuDNI_Vendedor().equals(DNI)) ? null : uV.getNombre(),s.getFecha(),false));
        }

        return DTOservices;

    }


}