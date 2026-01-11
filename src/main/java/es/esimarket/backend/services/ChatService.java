package es.esimarket.backend.services;
import es.esimarket.backend.dtos.ChatDTO;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.exceptions.CannotCreateChatError;
import es.esimarket.backend.repositories.ProductoRepository;
import es.esimarket.backend.repositories.UsuarioRepository;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.esimarket.backend.entities.Chat;
import es.esimarket.backend.repositories.ChatRepository;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
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

    public String CrearChat(String uDNI1, String uDNI2, int IdProducto)
    {

        if(uDNI1.equals(uDNI2)) throw new CannotCreateChatError("No se puede crear un chat con uno mismo");

        String sql = "Select Exists (Select 1 from chat where uDNIcomprador = ? and uDNIvendedor = ? and IdProducto = ?)";
        boolean existe = jdbcTemplate.queryForObject(sql, Boolean.class, uDNI1, uDNI2, IdProducto); //el boolean.true.equals es por si devuelve un null que no se ralle por que la clase Boolean no es lo mismo que boolean, es la que lo enmascara

        if(existe) throw new CannotCreateChatError("Ya existe un chat con estos usuarios y producto");

        Chat c = new Chat();
        c.setuDNIcomprador(uDNI1);
        c.setUDNIvendedor(uDNI2);
        c.setIdProducto(IdProducto);

        chatRepository.save(c);
        return "Chat creado exitosamente";


    }

    public List<ChatDTO> getChatsUsu(String dni) throws CannotCreateChatError{
        List<ChatDTO> chatDTOs = new ArrayList<>();
        List<Chat> chatEntities = chatRepository.findByUDNIcompradorOrUDNIvendedor(dni,dni);

        if (chatEntities.isEmpty()) throw new CannotCreateChatError("No tienes ningun chat iniciado");

        for(Chat c : chatEntities)
        {
            Usuario uOtro = usuarioRepository.findByid((dni.equals(c.getuDNIcomprador())) ? c.getUDNIvendedor() : c.getuDNIcomprador());
            Producto p = productoRepository.findByID(c.getIdProducto());

            chatDTOs.add(new ChatDTO(c.getId(),p.getNombre(),uOtro.getNombre(),uOtro.getApellidos(),uOtro.getCarrera(),p.getuDNI_Vendedor().equals(dni)));
        }

        return chatDTOs;

    }
}
