package es.esimarket.backend.services;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.AttributeBinderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.esimarket.backend.dtos.PedidosDTO;
import es.esimarket.backend.entities.Pedidos;
import es.esimarket.backend.repositories.PedidosRepository;
import es.esimarket.backend.mappers.PedidosMapper;

@Service
public class PedidosService{
    
    @Autowired
    private PedidosRepository pedidosRepository;

    @Autowired
    private PedidosMapper pedidosMapper;
    
    public List<PedidosDTO> mostrar_pedidos(){
        List<Pedidos> Pedidoss = pedidosRepository.findAll();
        List<PedidosDTO> PedidosDTOs = new ArrayList<>();

        for( Pedidos p : Pedidoss)
        {
            PedidosDTOs.add(pedidosMapper.toDto(p));
        }

        return PedidosDTOs;
    }
}