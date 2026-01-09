package es.esimarket.backend.controllers;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ProfileDataController {

    @Autowired
    private UsuarioRepository usuarioRepository;

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
