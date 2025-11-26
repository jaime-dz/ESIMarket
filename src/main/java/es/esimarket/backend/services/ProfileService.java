package es.esimarket.backend.services;
import es.esimarket.backend.controllers.responses.ProfileResponse;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.exceptions.CannotCreateUserError;
import es.esimarket.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService  jwtService;

    public ProfileResponse mostrar_perfil( String token ) throws CannotCreateUserError {

        String dni = jwtService.extraerDNI( token );
        Usuario u = usuarioRepository.findByid(dni);

        if ( u == null )  throw new CannotCreateUserError("Usuario no encontrado");

        return new ProfileResponse(u.getNombre(),u.getApellidos(),u.getId(),u.getCorreo(),u.getCarrera(),u.getSaldoMoneda());
    }
}
