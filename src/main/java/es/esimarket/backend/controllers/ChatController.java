package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.requests.ChatRequest;
import es.esimarket.backend.dtos.ChatDTO;
import es.esimarket.backend.services.AuthService;
import es.esimarket.backend.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import es.esimarket.backend.repositories.ChatRepository;
import es.esimarket.backend.repositories.MensajeRepository;
import es.esimarket.backend.services.ChatService;
import es.esimarket.backend.entities.Chat;

import java.util.List;

//a

@Controller
@RequestMapping("/chat")
public class ChatController
{
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private ChatService chatSercice;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/")
    public ResponseEntity<String> postChat(HttpServletRequest request , @RequestBody final ChatRequest Crequest)
    {
        String token = request.getHeader("Authorization").substring(7);
        String dniComp = jwtService.extraerDNI(token);

        return chatSercice.CrearChat(dniComp, Crequest.dni(),Crequest.idProd());
    }

    @GetMapping("/")
    public ResponseEntity<List<ChatDTO>> getChats(HttpServletRequest request)
    {
        return ResponseEntity.ok(chatSercice.getChatsUsu(request));
    }

    
}