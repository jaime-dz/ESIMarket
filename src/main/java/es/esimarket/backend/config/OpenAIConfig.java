package es.esimarket.backend.config;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    @Value("${openai.api-key}")
    private String apiKey;

    @Bean
    public OpenAIClient openAIClient() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("Debes definir openai.api-key en application.properties o env.properties");
        }

        return OpenAIOkHttpClient.builder()
                .apiKey(apiKey)         // <--- aquí
                .build();
    }
}