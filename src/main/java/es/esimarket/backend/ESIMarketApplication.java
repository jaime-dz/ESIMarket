package es.esimarket.backend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "es.esimarket.backend")
public class ESIMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(ESIMarketApplication.class, args);
	}

}

