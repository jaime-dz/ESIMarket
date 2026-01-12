package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.requests.ChatRequest;
import es.esimarket.backend.controllers.responses.MessageResponse;
import es.esimarket.backend.dtos.ChatDTO;
import es.esimarket.backend.entities.Mensaje;
import es.esimarket.backend.exceptions.CannotCreateChatError;
import es.esimarket.backend.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import es.esimarket.backend.repositories.ChatRepository;
import es.esimarket.backend.repositories.MensajeRepository;
import es.esimarket.backend.services.ChatService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.springframework.security.core.Authentication;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/chat")
public class ChatController
{
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/")
    public ResponseEntity<HashMap<String,String>> postChat(@RequestBody final ChatRequest Crequest) throws CannotCreateChatError
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dniComp = auth.getName();

        HashMap<String, String> response = new HashMap<>();

        String respuesta = null;

        try {
            respuesta = chatService.CrearChat(dniComp, Crequest.dni(),Crequest.idProd());
        }catch (CannotCreateChatError e){
            response.put("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("message",respuesta);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public String getChatsUser(){
        return "chat-list";
    }

    @MessageMapping("/chat.history")
    public void historial(@Payload Map<String, Integer> payload) {
        Integer chatId = payload.get("chatId");

        // Buscamos todos los mensajes de ese chat (ordenados por fecha si es posible)
        List<Mensaje> mensajes = mensajeRepository.findByIDChat(chatId, Sort.by(Sort.Direction.ASC, "fechaHora"));

        // Transformamos de Entidad -> DTO (MessageResponse)
        List<MessageResponse> historial = mensajes.stream()
                .map(m -> new MessageResponse(
                        m.getId(),
                        m.getTexto(),
                        m.getuDNIremitente(),     // Quién lo envió
                        calcularFechaAmigable(m.getFecha()),      // Fecha
                        m.getHoraMin(),  // Hora
                        null             // clientId (da igual para el historial)
                ))
                .collect(Collectors.toList());

        // Enviamos la lista al canal del chat
        messagingTemplate.convertAndSend("/topic/messages/" + chatId, historial);
    }

    @PostMapping("/user")
    @ResponseBody
    public ResponseEntity<List<ChatDTO>> getChats() throws CannotCreateChatError
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();

        return ResponseEntity.ok(chatService.getChatsUsu(dni));
    }

    private String calcularFechaAmigable(LocalDateTime fechaMensaje) {
        LocalDate fecha = fechaMensaje.toLocalDate();
        LocalDate hoy = LocalDate.now();

        if (fecha.equals(hoy)) {
            return "Hoy";
        } else if (fecha.equals(hoy.minusDays(1))) {
            return "Ayer";
        } else {
            // Devuelve formato dd/MM/yyyy
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return fecha.format(formatter);
        }
    }
    
}