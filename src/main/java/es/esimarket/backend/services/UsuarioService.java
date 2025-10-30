package es.esimarket.backend.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository userRepository;

    public ResponseEntity<String> registerUser( String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] salt = LoginEncriptado.GenerateSalt();

        Usuario user = new Usuario(username,LoginEncriptado.HashPassword(password, salt),salt);

        if (userRepository.existsById(user.getId())) {
            return ResponseEntity.ok("El dni ya está registrado");
        }

        userRepository.save(user);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    public ResponseEntity<String> loginUser(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException{

        Usuario u = userRepository.getReferenceById(username);

        if(u==null)
        {
            return ResponseEntity.ok("Usuario no encontrado");
        }
        
        String CodedPassword = LoginEncriptado.HashPassword(password, u.getSalt());
        if(!Objects.equals(u.getContrasenna(), CodedPassword))
        {
            return ResponseEntity.ok("La contraseña incorrecta");
        }


        return ResponseEntity.ok("Usuario registrado correctamente");
    }


}






