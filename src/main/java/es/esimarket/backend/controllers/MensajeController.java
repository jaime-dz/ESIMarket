package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.requests.MessageRequest;
import es.esimarket.backend.controllers.responses.MessageResponse;
import es.esimarket.backend.entities.Mensaje;
import es.esimarket.backend.exceptions.CannotDetermineIfToxicError;
import es.esimarket.backend.services.JwtService;
import es.esimarket.backend.services.OllamaService;
import es.esimarket.backend.services.VariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import es.esimarket.backend.repositories.ChatRepository;
import es.esimarket.backend.repositories.MensajeRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import es.esimarket.backend.services.MensajeService;

import org.springframework.jdbc.core.JdbcTemplate;

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
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private OllamaService ollamaService;

    @Autowired
    private VariosService variosService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JdbcTemplate jbdcTemplate;

    @PostMapping("/{chat}")
    @ResponseBody
    public List<MessageResponse> getMensajes(Model model, @PathVariable("chat") int chat){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();

       return mensajeService.mostrar_mensajes(mensajeRepository.findByIDChat(chat,Sort.by(Sort.Direction.ASC, "fechaHora")),dni);

    }

    @MessageMapping("/chat.sendMessage")
    public void recibirMensaje(@Payload Map<String, String> payload){

        Integer chatId = Integer.parseInt(String.valueOf(payload.get("chatId")));
        String texto = payload.get("message");
        String dni = payload.get("senderID");
        String clientId = payload.get("clientId");

        LocalDateTime FechaAct = variosService.ObtenerFecha();

        if ( mensajeService.ContienePalabrasProhibidas(texto) ){

            System.out.println("----------------------------------- Bloqueado por FILTRO LOCAL: " + texto + " ----------------------------------- ");

            MessageResponse respuestaError = new MessageResponse(
                    null,
                    "Tu mensaje ha sido bloqueado por contenido inapropiado.",
                    dni,
                    variosService.calcularFechaAmigable(FechaAct),
                    payload.get("hour"),
                    clientId,
                    true
            );


            messagingTemplate.convertAndSendToUser(dni, "/queue/errors", respuestaError);
            return;

        }

        CompletableFuture.runAsync(() -> {

            try{

                String prompt = "Detect toxicity, insults or hate speech. Respond ONLY 'true' if found, 'false' otherwise. No explanation. Text: ### ";
                String respuestaIA = null;
                try {
                    respuestaIA = ollamaService.isToxic(prompt + texto + " ###");
                } catch (CannotDetermineIfToxicError e) {
                    respuestaIA = "false";
                }

                boolean isToxic = respuestaIA.toLowerCase().contains("true");

                if (isToxic) {

                    System.out.println("----------------------------------- Bloqueado por IA (Ollama): " + texto + "----------------------------------- ");

                    MessageResponse respuestaError = new MessageResponse(
                            null,
                            "Tu mensaje ha sido bloqueado por contenido inapropiado.",
                            dni,
                            variosService.calcularFechaAmigable(FechaAct),
                            payload.get("hour"),
                            clientId,
                            true
                    );


                    messagingTemplate.convertAndSendToUser(dni, "/queue/errors", respuestaError);
                    return;
                }

                Mensaje m  = mensajeService.CrearMensaje(chatId,dni, texto, FechaAct);

                MessageResponse respuesta = new MessageResponse(
                        m.getId(),
                        m.getTexto(),
                        m.getuDNIremitente(),
                        variosService.calcularFechaAmigable(m.getFecha()),
                        m.getHoraMin(),
                        clientId,
                        false
                );

                messagingTemplate.convertAndSend("/topic/messages/" + chatId, respuesta);

            }catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error enviando mensaje socket: " + e.getMessage());
            }

        });

    }



}