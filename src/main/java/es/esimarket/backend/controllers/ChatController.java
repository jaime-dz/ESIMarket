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
    public ResponseEntity<HashMap<String,String>> postChat(@CookieValue(name = "accessToken", required = false) String accessToken , @RequestBody final ChatRequest Crequest) throws CannotCreateChatError
    {
        HashMap<String, String> response = new HashMap<>();

        String dniComp = jwtService.extraerDNI(accessToken);

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
    public ResponseEntity<List<ChatDTO>> getChats(@CookieValue(name = "accessToken", required = false) String accessToken) throws CannotCreateChatError
    {
        return ResponseEntity.ok(chatService.getChatsUsu(accessToken));
    }

    
}