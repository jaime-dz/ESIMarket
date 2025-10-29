package es.esimarket.backend.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository userRepository;

    public String registerUser(String user_string) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeySpecException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode usu_json = mapper.readTree(user_string);

        String id = usu_json.get("id").asText();
        String contrasenna = usu_json.get("contrasenna").asText();

        byte[] salt = LoginEncriptado.GenerateSalt();

        Usuario user = new Usuario(id,LoginEncriptado.HashPassword(contrasenna, salt),salt);

        if (userRepository.existsById(user.getId())) {
            return "El dni ya está registrado";
        }

        userRepository.save(user);

        return "Usuario registrado correctamente";
    }

    public String loginUser(Usuario user) {
        return "Usuario registrado correctamente";
    }


}






