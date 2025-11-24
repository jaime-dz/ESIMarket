package es.esimarket.backend.services;
import es.esimarket.backend.exceptions.CannotDetermineIfToxicError;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OllamaService {

    @Autowired
    private OllamaChatModel ollamaChatModel;


    public String isToxic( String prompt ) throws CannotDetermineIfToxicError {

        OllamaOptions options = new OllamaOptions();
        options.setModel("qwen2.5:1.5b");
        options.setTemperature(0f);

        ChatResponse response = ollamaChatModel.call(new Prompt(prompt,options));

        if ( response != null ){
            return response.getResult().getOutput().getContent();
        }

        throw new CannotDetermineIfToxicError("No se pudo determinar si el mensaje era toxico");
    }
}
