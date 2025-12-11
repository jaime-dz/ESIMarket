package es.esimarket.backend.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import es.esimarket.backend.entities.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer>{
    Servicio findByidProd(int id);
    Servicio findByidProdAndDNIcomprador(int idProd,String DNIcomprador);
    List<Servicio> findByDNIcompradorAndFinalizadoFalse(String DNIcomprador);
}