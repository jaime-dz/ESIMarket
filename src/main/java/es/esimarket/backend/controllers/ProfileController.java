package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.responses.ProfileResponse;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.exceptions.CannotCreateUserError;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.JwtService;
import es.esimarket.backend.services.LoginEncriptado;
import es.esimarket.backend.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LoginEncriptado loginEncriptado;

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

    @GetMapping("/edit/password")
    public String changePasswordView(){
        return "edit-password";
    }

    @PostMapping("/edit/password")
    public ResponseEntity<String> changePassword( @RequestBody String newPassword ){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uDNI = auth.getName();

        Usuario u = usuarioRepository.findById(uDNI).orElseThrow(()-> new CannotCreateUserError("Usuario no encontrado"));

        if ( loginEncriptado.matches(newPassword, Base64.getEncoder().encodeToString(u.getSalt()) + " " + u.getContrasenna()) ){
            return ResponseEntity.badRequest().body("La contraseña debe ser distinta a la anterior");
        }

        String[] credencialesNuevas = loginEncriptado.encode(Base64.getEncoder().encodeToString(u.getSalt()) + " " + newPassword).split(" ");
        byte[] newSalt = Base64.getDecoder().decode(credencialesNuevas[0]);
        String newHash = credencialesNuevas[1];

        u.setContrasenna(newHash);
        u.setSalt(newSalt);
        usuarioRepository.save(u);

        return  ResponseEntity.ok().body("Contraseña cambiada correctamente");

    }

    @PutMapping("/edit")
    public ResponseEntity<Void> modProfile(@RequestBody ProfileResponse p ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uDNI = auth.getName();
        profileService.editarUsuario(uDNI, p);
        return ResponseEntity.ok().build();
    }

}
