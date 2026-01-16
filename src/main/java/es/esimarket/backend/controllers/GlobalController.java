package es.esimarket.backend.controllers;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.exceptions.*;
import es.esimarket.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice
public class GlobalController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @ExceptionHandler(BadInputError.class)
    public ResponseEntity<Map<String, String>> handleBadInput(BadInputError e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler(CannotCreateUserError.class)
    public ResponseEntity<Map<String, String>> handleCannotCreateUser(CannotCreateUserError e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler(CannotCreateTokenError.class)
    public ResponseEntity<Map<String, String>> handleTokenError(CannotCreateTokenError e) {
        return buildInternalErrorResponse(e);
    }

    @ExceptionHandler(CannotCreateProductError.class)
    public ResponseEntity<Map<String, String>> handleProductError(CannotCreateProductError e) {
        return buildInternalErrorResponse(e);
    }

    @ExceptionHandler(CannotCreateChatError.class)
    public ResponseEntity<Map<String, String>> handleChatError(CannotCreateChatError e) {
        return buildInternalErrorResponse(e);
    }

    @ExceptionHandler(CannotCompleteActionError.class)
    public ResponseEntity<Map<String, String>> handleActionError(CannotCompleteActionError e) {
        return buildInternalErrorResponse(e);
    }

    @ExceptionHandler(CannotCompletePurchaseError.class)
    public ResponseEntity<Map<String, String>> handlePurchaseError(CannotCompletePurchaseError e) {
        return buildInternalErrorResponse(e);
    }

    @ExceptionHandler(CannotCreatePhotoError.class)
    public ResponseEntity<Map<String, String>> handlePhotoError(CannotCreatePhotoError e) {
        return buildInternalErrorResponse(e);
    }

    @ExceptionHandler(CannotDetermineIfToxicError.class)
    public ResponseEntity<Map<String, String>> handleToxicError(CannotDetermineIfToxicError e) {
        return buildInternalErrorResponse(e);
    }

    @ExceptionHandler(CannotMakeDonationError.class)
    public ResponseEntity<Map<String, String>> handleDonationError(CannotMakeDonationError e) {
        return buildInternalErrorResponse(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("message", "Ocurrió un error interno inesperado: " + e.getMessage()));
    }

    private ResponseEntity<Map<String, String>> buildInternalErrorResponse(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("message", e.getMessage()));
    }



    @ModelAttribute("profile")
    public Usuario populateUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            String dni = auth.getName();
            return usuarioRepository.findById(dni).orElse(null);
        }

        return null; // Si no está logueado, "profile" será null en la vista
    }
}
