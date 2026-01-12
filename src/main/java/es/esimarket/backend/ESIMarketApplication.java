package es.esimarket.backend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "es.esimarket.backend")
@EnableScheduling
public class ESIMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(ESIMarketApplication.class, args);
	}

}

