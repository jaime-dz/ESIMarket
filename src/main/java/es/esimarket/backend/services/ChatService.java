package es.esimarket.backend.services;
import es.esimarket.backend.dtos.ChatDTO;
import es.esimarket.backend.entities.Producto;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.mappers.ChatMapper;
import es.esimarket.backend.repositories.ProductoRepository;
import es.esimarket.backend.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private ChatMapper chatMapper;

    public ResponseEntity<String> CrearChat(String uDNI1,String uDNI2,int IdProducto)
    {

        Chat c = new Chat();

        if(uDNI1.compareTo(uDNI2) != 0)  //se puede hacer un poco mas eficiente
        {
            c.setuDNI1(uDNI1);
            c.setuDNI2(uDNI2);
            c.setIdProducto(IdProducto);

            if(uDNI1.compareTo(uDNI2) > 0)
            {
                c.setuDNI_Mayor(uDNI1);
                c.setuDNI_Menor(uDNI2);
            }
            else
            {
                c.setuDNI_Mayor(uDNI2);
                c.setuDNI_Menor(uDNI1);
            }

            String sql = "Select Exists (Select 1 from Chat where uDNIMayor = ? and uDNIMenor = ? and IdProducto = ?)";
            boolean existe = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class,c.getuDNI_Mayor(),c.getuDNI_Menor(),IdProducto)); //el boolean.true.equals es por si devuelve un null que no se ralle por que la clase Boolean no es lo mismo que boolean, es la que lo enmascara

            if(!existe)
            {
                chatRepository.save(c);
                return ResponseEntity.ok("Chat creado exitosamente");
            }
            else
                return ResponseEntity.ok("Ya existe un chat con dichos usuarios y producto");
        }
        else
            return ResponseEntity.ok("No se puede crear un chat contigo mismo");
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

    public List<ChatDTO> getChatsUsu(HttpServletRequest request){
        List<ChatDTO> chatDTOs = new ArrayList<>();

        String token = request.getHeader("Authorization").substring(7);
        String dni = jwtService.extraerDNI(token);

        List<Chat> chatEntities = chatRepository.findByUDNI1OrUDNI2(dni,dni);

        for(Chat chatEntity : chatEntities)
        {
            String otroDni;

            if ( chatEntity.getuDNI1().equals(dni) ){
                otroDni=chatEntity.getuDNI2();
            }else{
                otroDni=chatEntity.getuDNI1();
            }

            Usuario u = usuarioRepository.findByid(otroDni);
            Producto p = productoRepository.findByid(chatEntity.getIdProducto());

            chatDTOs.add(chatMapper.toDTO(chatEntity,u,p));
        }

        return chatDTOs;

    }
}
