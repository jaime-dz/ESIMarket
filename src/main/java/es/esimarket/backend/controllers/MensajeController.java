package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.requests.MessageRequest;
import es.esimarket.backend.controllers.responses.MessageResponse;
import es.esimarket.backend.entities.Chat;
import es.esimarket.backend.entities.Mensaje;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.exceptions.CannotDetermineIfToxicError;
import es.esimarket.backend.repositories.ProductoRepository;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.*;
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
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VariosService variosService;

    @Autowired
    private NotificationService notificationService;

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

                String prompt = """
                    Spanish Moderator. Return 'true' ONLY for clear insults.
                    Return 'false' for: pets (perro, chapi), family (hijo), and repeated letters (holaaa).
                    Text: ###\s""" + texto + " ###\nResult:";

                String respuestaIA = null;
                try {
                    respuestaIA = ollamaService.isToxic(prompt + texto + " ###");
                } catch (Exception e) {
                    respuestaIA = "false";
                }

                boolean isToxic = respuestaIA.toLowerCase().trim().contains("true");

                if (isToxic) {

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

                Usuario u = usuarioRepository.findByid(m.getuDNIremitente());
                Chat c = chatRepository.findByid(chatId);
                Producto p = productoRepository.findByID(c.getIdProducto());
                String destinatarioDni = obtenerDniDestinatario(chatId,m.getuDNIremitente());
                notificationService.notificarNuevoMensaje(
                        destinatarioDni,
                        u.getNombre(),
                        p.getNombre()
                );

            }catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error enviando mensaje socket: " + e.getMessage());
            }

        });

    }

    private String obtenerDniDestinatario( int idChat , String remitente){

        Chat c = chatRepository.findByid(idChat);

        return ( c.getuDNIcomprador().equals(remitente))  ? c.getUDNIvendedor() : c.getuDNIcomprador() ;
    }



}