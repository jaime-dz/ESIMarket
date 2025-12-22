package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.requests.MessageRequest;
import es.esimarket.backend.dtos.MensajeDTO;
import es.esimarket.backend.exceptions.CannotDetermineIfToxicError;
import es.esimarket.backend.services.JwtService;
import es.esimarket.backend.services.OllamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import es.esimarket.backend.repositories.ChatRepository;
import es.esimarket.backend.repositories.MensajeRepository;

import java.util.*;

import es.esimarket.backend.services.MensajeService;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/messages")
public class MensajeController
{
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private OllamaService ollamaService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JdbcTemplate jbdcTemplate;

    @GetMapping("/{chat}")
    public ResponseEntity<List<MensajeDTO>> getMensajes(@PathVariable("chat") int chat){
        return ResponseEntity.ok(mensajeService.mostrar_mensajes(mensajeRepository.findByIDChat(chat,Sort.by(Sort.Direction.ASC, "fechaHora"))));

    }

    @PostMapping("/")
    public ResponseEntity<HashMap<String, String>> postMensajes( @RequestBody final MessageRequest Mrequest){

        HashMap<String, String> response = new HashMap<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();

        String prompt = "Detect toxicity, insults or hate speech. Respond ONLY 'true' if found, 'false' otherwise. No explanation. Text: ";


        String respuestaIA = null;
        try {
            respuestaIA = ollamaService.isToxic(prompt + Mrequest.Texto());
        } catch (CannotDetermineIfToxicError e) {
            response.put("error", e.getMessage() );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        boolean isToxic = Boolean.parseBoolean(respuestaIA);

        if ( isToxic ){
            response.put("error", "Tu mensaje contiene toxicidad, hijo de puta" );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String status = mensajeService.CrearMensaje(Mrequest.idChat(), dni, Mrequest.Texto());

        response.put("message", status);

        return ResponseEntity.ok(response);
    }



}