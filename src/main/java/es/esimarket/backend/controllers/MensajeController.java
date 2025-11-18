package es.esimarket.backend.controllers;

import com.openai.client.OpenAIClient;
import es.esimarket.backend.controllers.requests.MessageRequest;
import es.esimarket.backend.dtos.MensajeDTO;
import es.esimarket.backend.services.JwtService;
import es.esimarket.backend.services.OpenAIService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    /*
    @Autowired
    private OpenAIService openAIService;
    */
    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JdbcTemplate jbdcTemplate;

    @GetMapping("/{chat}")
    public ResponseEntity<List<MensajeDTO>> getMensajes(@PathVariable("chat") int chat){

        return ResponseEntity.ok(mensajeRepository.findByid_IDChat(chat,Sort.by(Sort.Direction.ASC, "id.fechaHora")));

    }
    /*
    @PostMapping("/")
    public ResponseEntity<String> postMensajes(HttpServletRequest request, @RequestBody final MessageRequest Mrequest){

        String token = request.getHeader("Authorization").substring(7);
        String dni = jwtService.extraerDNI(token);

        if(openAIService.isToxic(Mrequest.Texto()))
            return ResponseEntity.badRequest()
                    .body("Tu mensaje contiene palabras prohibidas, hijo de puta");
        
        return mensajeService.CrearMensaje(Mrequest.idChat(), dni, Mrequest.Texto());
    }

     */

}