package es.esimarket.backend.services;

import es.esimarket.backend.dtos.NotificacionDTO;
import es.esimarket.backend.entities.Notificacion;
import es.esimarket.backend.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void crearYEnviarNotificacion(String targetDni, String mensaje) {
        // 1. Guardar en Base de Datos
        Notificacion notificacion = new Notificacion();
        notificacion.setuDNI(targetDni);
        notificacion.setMensaje(mensaje);

        Notificacion guardada = notificationRepository.save(notificacion);

        // 2. Convertir a DTO
        NotificacionDTO dto = new NotificacionDTO(guardada);

        // 3. Enviar por WebSocket al usuario específico
        // Spring convierte "/queue/notifications" en "/user/{username}/queue/notifications" internamente
        messagingTemplate.convertAndSendToUser(targetDni, "/queue/notifications", dto);
    }

    public void notificarNuevoMensaje(String destinatarioDni, String nombreEmisor, String nombreProd) {
        String textoNotif = "Tienes mensajes sin leer de " + nombreEmisor + " sobre " + nombreProd;

        boolean yaNotificado = notificationRepository.existsByuDNIAndMensaje(destinatarioDni, textoNotif);

        if (!yaNotificado) {
            // 2. Solo si no existe, la creamos y la enviamos
            Notificacion n = new Notificacion();
            n.setuDNI(destinatarioDni);
            n.setMensaje(textoNotif);

            Notificacion guardada = notificationRepository.save(n);

            // 3. Enviar por WebSocket
            messagingTemplate.convertAndSendToUser(destinatarioDni, "/queue/notifications", new NotificacionDTO(guardada));
        }
    }
}
