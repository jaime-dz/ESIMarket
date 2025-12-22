package es.esimarket.backend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.esimarket.backend.dtos.ServicioDTO;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.exceptions.CannotCreateProductError;
import es.esimarket.backend.mappers.ServiceMapper;
import es.esimarket.backend.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;

import es.esimarket.backend.entities.Servicio;

import es.esimarket.backend.repositories.ServicioRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioService{

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private VariosService variosService;

    public void CrearServicioPendiente(int idProd, String DNIcomprador)
    {
        LocalDateTime ahora = LocalDateTime.now();
        Servicio s = new Servicio(idProd,DNIcomprador,ahora,false);

        servicioRepository.save(s);
    }

    public String modificarFecha(int idProd, String DNIcomprador,LocalDateTime fecha)
    {
        Servicio s = servicioRepository.findByidProdAndDNIcomprador(idProd, DNIcomprador);

        s.setFecha(fecha);

        servicioRepository.save(s);

        return "Se ha modificado la fecha";
    }

    @Transactional
    public String finalizarServicio(int idProd, String DNIcomprador)
    {
        Servicio s = servicioRepository.findByidProdAndDNIcomprador(idProd, DNIcomprador);
        Producto p = productoRepository.findByID(idProd);

        s.setFinalizado(true);

        servicioRepository.save(s);
        if ( p != null ) productoRepository.delete(p);

        return "Se ha finalizado el servicio";
    }

    public List<ServicioDTO> mostrar_servicios_usuario(String DNIcomprador)
    {
        List<Servicio> services = servicioRepository.findByDNIcompradorAndFinalizadoFalse(DNIcomprador);
        List<ServicioDTO> DTOservices = new ArrayList<>();

        for( Servicio s : services){

            Producto p = productoRepository.findById(s.getIdProd()).orElseThrow(()-> new CannotCreateProductError("El producto no existe"));
            DTOservices.add(serviceMapper.toDTO(s,p));
        }

        return DTOservices;

    }


}