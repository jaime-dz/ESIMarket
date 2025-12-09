package es.esimarket.backend.controllers;
import es.esimarket.backend.controllers.responses.ProfileResponse;
import es.esimarket.backend.exceptions.CannotCreateUserError;
import es.esimarket.backend.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/")
    public ResponseEntity<ProfileResponse> getProfile( @CookieValue(name = "accessToken") String accessToken ) throws CannotCreateUserError {
        return ResponseEntity.ok(profileService.mostrar_perfil(accessToken));
    }

    @PostMapping("/edit")
    public String modProfile() { return "profile-edit"; }

}
