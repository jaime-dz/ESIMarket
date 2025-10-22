package es.madmonkeymarket.backend;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.sql.Connection;


@Component
public class DatabaseCheck implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseCheck(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            String currentDb = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
            System.out.println("✅ Conectado a la base de datos real: " + currentDb);

            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM usuarios", Integer.class);
            System.out.println("📊 Usuarios encontrados en esa base: " + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
