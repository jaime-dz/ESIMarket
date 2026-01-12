package es.esimarket.backend.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name="token")
@AllArgsConstructor
public class Token {

    public enum TokenType {
        BEARER;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "JwTToken", unique = true)
    private String token;

    @Column(name="Tipo")
    @Enumerated(EnumType.STRING)
    private TokenType type = TokenType.BEARER;

    @Column(name="Revocado")
    private boolean revocado;

    @Column(name="Expirado")
    private boolean expirado;

    @Column(name="uDNI")
    private String user;

    @Column(name="fechaExpiracion")
    private Date fechaExpiracion;


    public Token() {}

    public Token(String token, TokenType type, boolean revocado, boolean expirado, String user, Date fechaExpiracion) {
        this.token = token;
        this.type = type;
        this.revocado = revocado;
        this.expirado = expirado;
        this.user = user;
        this.fechaExpiracion = fechaExpiracion;
    }

    public Long getID() { return id; }

    public String getToken() { return token; }
    public void setToken(String contrasenna) { this.token = token; }

    public TokenType getTipo() { return type; }
    public void setTipo(TokenType type) { this.type = type; }

    public boolean getRevocado() { return revocado; }
    public void setRevocado(boolean revocado) { this.revocado = revocado; }

    public boolean getExpirado() { return expirado; }
    public void setExpirado(boolean expirado) { this.expirado = expirado; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public Date getFechaExpiracion() {return fechaExpiracion;}
    public void setFechaExpiracion(Date fechaExpiracion) {this.fechaExpiracion = fechaExpiracion;}


}
