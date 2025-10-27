package es.esimarket.backend.services;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository userRepository;

    public String registerUser(Usuario user) {
        if (userRepository.existsById(user.getId())) {
            return "El dni ya está registrado";
        }

        byte[] salt = LoginEncriptado.GenerateSalt();
        user.setSalt(salt);

        try {
            user.setContrasenna(LoginEncriptado.HashPassword(user.getContrasenna(), salt));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Error al registrar usuario";
        }

        userRepository.save(user);

        return "Usuario registrado correctamente";
    }

    public String loginUser(Usuario user) {
        return "Usuario registrado correctamente";
    }


}






