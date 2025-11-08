package es.esimarket.backend.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import es.esimarket.backend.repositories.ChatRepository;
import es.esimarket.backend.repositories.MensajeRepository;
import es.esimarket.backend.services.ChatService;

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

    @PostMapping("/")
    public ResponseEntity<String> postChat(@RequestParam String uDNI1,@RequestParam String uDNI2)
    {
        //Falta poner los uDNI menores y mayores y la variable de autoincremento
        return chatSercice.CrearChat(uDNI1, uDNI2);
    }


    
}