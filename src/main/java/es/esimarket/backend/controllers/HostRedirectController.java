package es.esimarket.backend.controllers;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HostRedirectController {

    @RequestMapping("/**")
    public String redirectBasedOnHost(HttpServletRequest request) {
        String host = request.getHeader("Host");
        if ("esimarket.es".equals(host) || "www.esimarket.es".equals(host)) {
            return "redirect:https://esimarket.es/home/";
        }
        return null; // deja que Spring Boot maneje normalmente
    }
}