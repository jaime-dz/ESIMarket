package es.madmonkeymarket.backend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "es.madmonkeymarket.backend")
public class MadMonkeyMarketBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MadMonkeyMarketBackendApplication.class, args);
	}

}
