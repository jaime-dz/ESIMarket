package es.esimarket.backend.services;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import es.esimarket.backend.entities.Chat;
import es.esimarket.backend.repositories.ChatRepository;

@Service
public class ChatService{

    private ChatRepository chatRepository;

    public ResponseEntity<String> CrearChat(String uDNI1,String uDNI2)
    {
        Chat c = new Chat();

        c.setuDNI1(uDNI1);
        c.setuDNI2(uDNI2);

        //falta ponerle el id de autoincremento y los dos udni raros

        chatRepository.save(c);

        return ResponseEntity.ok("Chat creado exitosamente");
    }
}
