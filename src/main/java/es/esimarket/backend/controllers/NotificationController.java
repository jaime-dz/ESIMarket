package es.esimarket.backend.controllers;
import es.esimarket.backend.dtos.NotificacionDTO;
import es.esimarket.backend.entities.Notificacion;
import es.esimarket.backend.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificacionDTO>> obtenerMisNotificaciones() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();

        // Suponiendo que tienes un método en el repositorio para buscar por DNI
        // Si no lo tienes, añádelo en NotificationRepository: List<Notificacion> findByuDNI(String uDNI);
        List<Notificacion> notificaciones = notificationRepository.findByuDNI(dni);

        // Convertir a DTO
        List<NotificacionDTO> dtos = notificaciones.stream()
                .map(NotificacionDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable BigInteger id) {
        notificationRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-all/")
    public ResponseEntity<Void> deleteAllNotifications() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();

        notificationRepository.deleteByuDNI(dni);
        return ResponseEntity.ok().build();
    }

}
