package es.esimarket.backend.controllers;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.exceptions.CannotCreateTokenError;
import es.esimarket.backend.exceptions.CannotCreateUserError;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public String index(@CookieValue(name = "accessToken", required = false) String accessToken, Model model)
    {
        if ( !accessToken.isEmpty() )
        {
            String dni = jwtService.extraerDNI(accessToken);
            Usuario u = usuarioRepository.findById(dni).orElseThrow(() -> new CannotCreateUserError("Usuario no encontrado"));

            model.addAttribute("profile.",u);
        }

        return "index";
    }

    @GetMapping("/about")
    public String about() { return "about"; }

    @GetMapping("/PrivacyAndPolicy")
    public String privacyAndPolicy() { return "forward:/ESIMarket_Politica_Privacidad.pdf"; }

    @GetMapping("/TermsAndConditions")
    public String termsAndConditions() { return "forward:/ESIMarket_Terminos_y_Condiciones.pdf"; }

}
