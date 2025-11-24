package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.requests.ChatRequest;
import es.esimarket.backend.dtos.ChatDTO;
import es.esimarket.backend.exceptions.CannotCreateChatError;
import es.esimarket.backend.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import es.esimarket.backend.repositories.ChatRepository;
import es.esimarket.backend.repositories.MensajeRepository;
import es.esimarket.backend.services.ChatService;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController
{
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/")
    public ResponseEntity<HashMap<String,String>> postChat(@RequestHeader(HttpHeaders.AUTHORIZATION) String request , @RequestBody final ChatRequest Crequest) throws CannotCreateChatError
    {
        HashMap<String, String> response = new HashMap<>();

        String token = request.substring(7);
        String dniComp = jwtService.extraerDNI(token);

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
    public ResponseEntity<List<ChatDTO>> getChats(@RequestHeader(HttpHeaders.AUTHORIZATION) String request) throws CannotCreateChatError
    {
        return ResponseEntity.ok(chatService.getChatsUsu(request));
    }

    
}