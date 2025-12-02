package es.esimarket.backend.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/user")
    public String user() {

    }

    @GetMapping("/about")
    public String about() { return "about"; }

    @GetMapping("/PrivacyAndPolicy")
    public String privacyAndPolicy() { return "forward:/ESIMarket_Politica_Privacidad"; }

    @GetMapping("/TermsAndConditions")
    public String termsAndConditions() { return "forward:/ESIMarket_Terminos_y_Condiciones"; }

}
