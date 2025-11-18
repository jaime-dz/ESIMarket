package es.esimarket.backend.services;
import com.openai.client.OpenAIClient;
import com.openai.models.moderations.Moderation;
import com.openai.models.moderations.ModerationCreateParams;
import com.openai.models.moderations.ModerationCreateResponse;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

    /*
    private final OpenAIClient client;

    public OpenAIService(OpenAIClient client) {
        this.client = client;
    }

    public boolean isToxic(String text) {

        ModerationCreateParams params = ModerationCreateParams.builder()
                .model("omni-moderation-latest")
                .input(text)
                .build();

        ModerationCreateResponse response = client.moderations().create(params);

        Moderation result = response.results().getFirst();

        return result.flagged();
    }

     */
}


