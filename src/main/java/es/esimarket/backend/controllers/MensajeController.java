package es.esimarket.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import es.esimarket.backend.repositories.ChatRepository;
import es.esimarket.backend.repositories.MensajeRepository;

import java.util.*;

import es.esimarket.backend.services.MensajeService;

import org.springframework.jdbc.core.JdbcTemplate;

@Controller
@RequestMapping("/mensajes")
public class MensajeController
{
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private JdbcTemplate jbdcTemplate;

    @GetMapping("/{chat}")
    public ResponseEntity<List<String>> getMensajes(@PathVariable("chat") int chat){
        //return ResponseEntity.ok(mensajeRepository.findByid_IDChat(chat));

        String sql = new String("SELECT Texto FROM Mensajes WHERE IDChat= ? ORDER BY FechaHora ASC ");
        return ResponseEntity.ok(jbdcTemplate.queryForList(sql,String.class,chat));
    }

    @PostMapping("{chat}/{uDNI}/{texto}")
    public ResponseEntity<String> postMensajes(@RequestParam int chat,@RequestParam String uDNI,@RequestParam String texto){
        if(mensajeService.ContienePalabrasProhibidas(texto))
            return ResponseEntity.ok("Tu mensaje contiene palabras prohibidas, hijo de puta");
        
        return mensajeService.CrearMensaje(chat, uDNI, texto);
    }

}