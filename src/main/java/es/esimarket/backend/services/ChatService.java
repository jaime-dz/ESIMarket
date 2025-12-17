package es.esimarket.backend.services;
import es.esimarket.backend.dtos.ChatDTO;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.exceptions.CannotCreateChatError;
import es.esimarket.backend.exceptions.CannotCreateTokenError;
import es.esimarket.backend.mappers.ChatMapper;
import es.esimarket.backend.repositories.ProductoRepository;
import es.esimarket.backend.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.esimarket.backend.entities.Chat;
import es.esimarket.backend.repositories.ChatRepository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ChatService{

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ChatMapper chatMapper;

    public String CrearChat(String uDNI1, String uDNI2, int IdProducto)
    {
        Chat c = new Chat();

        if(uDNI1.equals(uDNI2)) throw new CannotCreateChatError("No se puede crear un chat con uno mismo");

        String sql = "Select Exists (Select 1 from Chat where uDNIcomprador = ? and uDNIvendedor = ? and IdProducto = ?)";
        boolean existe = jdbcTemplate.queryForObject(sql, Boolean.class, c.getuDNIcomprador(), c.getUDNIvendedor(), IdProducto); //el boolean.true.equals es por si devuelve un null que no se ralle por que la clase Boolean no es lo mismo que boolean, es la que lo enmascara

        if(existe) throw new CannotCreateChatError("Ya existe un chat con estos usuarios y producto");
        chatRepository.save(c);
        return "Chat creado exitosamente";


    }

    public ResponseEntity<Chat> getChat(String uDNI1,String uDNI2,int IdProducto)
    {
        String uDNIMayor, uDNIMenor;

        if(uDNI1.compareTo(uDNI2) > 0)
            {
                uDNIMayor=uDNI1;
                uDNIMenor=uDNI2;
            }
            else
            {
                uDNIMayor=uDNI2;
                uDNIMenor=uDNI1;
            }

        String sql = "Select * from Chat where uDNIMayor = ? and uDNIMenor = ? and IdProducto = ?";
        return ResponseEntity.ok(jdbcTemplate.queryForObject(sql, Chat.class,uDNIMayor,uDNIMenor,IdProducto));
        

    }

    public List<ChatDTO> getChatsUsu(String dni) throws CannotCreateChatError{
        List<ChatDTO> chatDTOs = new ArrayList<>();
        List<Chat> chatEntities = chatRepository.findByUDNIcompradorOrUDNIvendedor(dni,dni);

        if (chatEntities.isEmpty()) throw new CannotCreateChatError("No tienes ningun chat iniciado");

        for(Chat chatEntity : chatEntities)
        {
            String otroDni;

            if ( chatEntity.getuDNIcomprador().equals(dni) ){
                otroDni=chatEntity.getUDNIvendedor();
             }else{
                otroDni=chatEntity.getuDNIcomprador();
            }

            Usuario u = usuarioRepository.findByid(otroDni);
            Producto p = productoRepository.findByID(chatEntity.getIdProducto());

            chatDTOs.add(chatMapper.toDTO(chatEntity,u,p));
        }

        return chatDTOs;

    }
}
