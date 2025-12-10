package es.esimarket.backend.services;
import es.esimarket.backend.controllers.responses.ProfileResponse;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.exceptions.CannotCreateUserError;
import es.esimarket.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ProfileService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService  jwtService;

    public void editarUsuario( String token , ProfileResponse request ) throws CannotCreateUserError {

        String dni = jwtService.extraerDNI( token );
        Usuario u = usuarioRepository.findByid(dni);

        if ( u == null )  throw new CannotCreateUserError("Usuario no encontrado");

        if (request.getNombre() != null) {
            u.setNombre(request.getNombre());
        }

        if (request.getApellidos() != null) {
            u.setApellidos(request.getApellidos());
        }

        if (request.getEmail() != null) {
            u.setCorreo(request.getEmail());
        }

        if (request.getCarrera() != null) {
            u.setCarrera(request.getCarrera());
        }

    }

}
