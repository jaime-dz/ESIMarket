package es.esimarket.backend.controllers;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HostRedirectController {

    @GetMapping("/")  // Solo intercepta la raíz
    public String redirectRoot(HttpServletRequest request) {
        String host = request.getHeader("Host");

        // Si el host es esimarket.es o www.esimarket.es
        if ("esimarket.es".equals(host) || "www.esimarket.es".equals(host)) {
            return "redirect:/home/";  // Redirige al path /home/
        }

        // Devuelve la vista por defecto si es otro host
        return "redirect:/home/";  // Por ejemplo index.html en templates
    }
}
