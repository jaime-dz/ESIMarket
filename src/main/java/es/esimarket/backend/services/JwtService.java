package es.esimarket.backend.services;
import es.esimarket.backend.entities.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    @Autowired
    private UserDetailsService userDetailsService;

    public String generateToken ( final Usuario user )
    {
        return buildToken(user,jwtExpiration);
    }

    public String generateRefreshToken ( final Usuario user )
    {
        return buildToken(user,refreshExpiration);
    }


    public String extraerDNI( final String token ){
        final Claims jwtToken = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return jwtToken.getSubject();
    }

    public Date extraerExpiracion( final String token ){
        final Claims jwtToken = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return jwtToken.getExpiration();
    }


    public boolean isTokenValid( final String token , final Usuario user ) {
        final String dni = extraerDNI(token);
        return (dni.equals(user.getId())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired( final String token ){
        return extraerExpiracion(token).before(new Date());
    }


    private String buildToken( final Usuario user, final long expiration ){


        return Jwts.builder()
                .claim("Nombre",user.getId())
                .claim("Apellidos",user.getApellidos())
                .claim("Roles", userDetailsService.loadUserByUsername(user.getId()).getAuthorities())
                .setSubject(user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSecretKey())
                .compact();
    }


    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
