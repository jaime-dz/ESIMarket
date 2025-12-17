package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.responses.ProfileResponse;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.exceptions.CannotCreateUserError;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.JwtService;
import es.esimarket.backend.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/")
    public String getProfile( Model model) throws CannotCreateUserError {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Usuario u = usuarioRepository.findByid(dni);

        if ( u == null )  throw new CannotCreateUserError("Usuario no encontrado");

        model.addAttribute("profile", new ProfileResponse(u.getNombre(),u.getApellidos(),u.getId(),u.getCorreo(),u.getCarrera(),u.getSaldoMoneda()));

        return "profile";
    }

    @GetMapping("/edit")
    public String editProfile(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Usuario u = usuarioRepository.findByid(dni);

        model.addAttribute("profile", new ProfileResponse(u.getNombre(),u.getApellidos(),u.getId(),u.getCorreo(),u.getCarrera(),u.getSaldoMoneda()));

        return "profile-edit";
    }

    @PutMapping("/edit")
    public ResponseEntity<Void> modProfile(@RequestBody ProfileResponse p ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uDNI = auth.getName();
        profileService.editarUsuario(uDNI, p);
        return ResponseEntity.ok().build();
    }

}
