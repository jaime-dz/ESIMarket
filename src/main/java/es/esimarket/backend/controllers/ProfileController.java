package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.responses.ProfileResponse;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.exceptions.CannotCreateUserError;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.AuthService;
import es.esimarket.backend.services.JwtService;
import es.esimarket.backend.services.LoginEncriptado;
import es.esimarket.backend.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    private AuthService authService;

    @Autowired
    private LoginEncriptado loginEncriptado;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/")
    public String getProfile( Model model) throws CannotCreateUserError {

        /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Usuario u = usuarioRepository.findByid(dni);

        if ( u == null )  throw new CannotCreateUserError("Usuario no encontrado");

        model.addAttribute("profileD", new ProfileResponse(u.getNombre(),u.getApellidos(),u.getId(),u.getCorreo(),u.getCarrera(),u.getSaldoMoneda()));
        */
        return "profile";
    }

    @GetMapping("/edit")
    public String editProfile(Model model)
    {
        return "profile-edit";
    }

    @GetMapping("/edit/password")
    public String changePasswordView(){
        return "edit-password";
    }

    @PostMapping("/edit/password")
    @ResponseBody
    public ResponseEntity<String> changePassword( @RequestParam(value = "newPassword") String newPassword ){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uDNI = auth.getName();

        Usuario u = usuarioRepository.findById(uDNI).orElseThrow(()-> new CannotCreateUserError("Usuario no encontrado"));

        if ( loginEncriptado.matches(newPassword, Base64.getEncoder().encodeToString(u.getSalt()) + " " + u.getContrasenna()) ){
            return ResponseEntity.badRequest().body("La contraseña debe ser distinta a la anterior");
        }

        byte[] newSalt = LoginEncriptado.GenerateSalt();
        String[] credencialesNuevas = loginEncriptado.encode(Base64.getEncoder().encodeToString(newSalt) + " " + newPassword).split(" ");
        String newHash = credencialesNuevas[1];

        u.setContrasenna(newHash);
        u.setSalt(newSalt);
        usuarioRepository.save(u);

        return  ResponseEntity.ok().body("Contraseña cambiada correctamente");

    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uDNI = auth.getName();

        authService.eliminar_usuario(uDNI);

        ResponseCookie jwtCookie = crearCookie("accessToken", "", 0,true);
        ResponseCookie refreshCookie = crearCookie("refreshToken", "", 0,true);
        ResponseCookie isLoggedIn = crearCookie("isLoggedIn", null,0,false);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .header(HttpHeaders.SET_COOKIE,isLoggedIn.toString())
                .build();

    }

    @PutMapping("/edit")
    @ResponseBody
    public ResponseEntity<Void> modProfile(@RequestBody ProfileResponse p ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uDNI = auth.getName();
        profileService.editarUsuario(uDNI, p);
        return ResponseEntity.ok().build();
    }

    private ResponseCookie crearCookie(String nombre, String valor, long duracion, boolean httponly) {
        return ResponseCookie.from(nombre, valor)
                .httpOnly(httponly) // Seguridad: JS no puede leerla
                .secure(false)  // false para localhost, true para producción (HTTPS)
                .path("/")
                .maxAge(duracion / 1000) // Segundos
                .sameSite("Strict")
                .build();

    }

}
