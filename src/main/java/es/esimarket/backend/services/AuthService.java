package es.esimarket.backend.services;
import es.esimarket.backend.controllers.requests.LoginRequest;
import es.esimarket.backend.controllers.requests.RegisterRequest;
import es.esimarket.backend.controllers.autenticacion.TokenResponse;
import es.esimarket.backend.entities.Donaciones;
import es.esimarket.backend.entities.Token;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.dtos.UsuarioDTO;
import es.esimarket.backend.exceptions.CannotCreateTokenError;
import es.esimarket.backend.exceptions.CannotCreateUserError;
import es.esimarket.backend.mappers.UserMapper;
import es.esimarket.backend.repositories.DonacionesRepository;
import es.esimarket.backend.repositories.TokenRepository;
import es.esimarket.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;


@Service
public class AuthService {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private DonacionesRepository donacionesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Scheduled(cron = "0 0 * * * ?") // Cada hora
    public void actualizarTokens() {
        tokenRepository.actualizarTokensExpirados(LocalDateTime.now());
    }

    public TokenResponse registerUser(RegisterRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException, CannotCreateUserError {

        if ( !request.username().matches("^[uU]\\d{8}$")) throw new CannotCreateUserError("Usuario invalido, debe de ser u + nºdni");

        byte[] salt = LoginEncriptado.GenerateSalt();
        String password = passwordEncoder.encode(Base64.getEncoder().encodeToString(salt) + " " + request.password());

        Usuario user = new Usuario(request.username(),
                           password.split(" ")[1],
                           request.email(),
                           request.name(),
                           request.apellidos(),
                           request.carrera(),
                           salt,
                           "ROLE_USER");

        if (userRepository.existsById(user.getId())) {
            throw new CannotCreateUserError("El usuario ya existe");
        }

        Donaciones d = new Donaciones(user.getId(),0);
        donacionesRepository.save(d);

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(savedUser.getId(), refreshToken);

        return new TokenResponse(jwtToken,refreshToken);
    }

    public TokenResponse loginUser(LoginRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException, CannotCreateUserError{

        Usuario u = userRepository.findByid(request.username());

        // SOLO PARA DESARROLLO. MODIFCAR AL FINAL

        if(u==null) {
            throw new CannotCreateUserError("Usuario o contraseña incorrectos ( USU NO EMCONTRADO ) ");
        }

        if (!passwordEncoder.matches(request.password(), Base64.getEncoder().encodeToString(u.getSalt()) + " " + u.getContrasenna())) {
            throw new CannotCreateUserError("Usuario o contraseña incorrectos");
        }

        var jwtToken = jwtService.generateToken(u);
        var refreshToken = jwtService.generateRefreshToken(u);

        saveUserToken(u.getId(),refreshToken);

        return new TokenResponse(jwtToken,refreshToken);

    }

    @Transactional
    public void logout_user( String refreshToken ){

        tokenRepository.deleteByToken(refreshToken);

    }

    public void saveUserToken( String user , String jwtToken ){

        var token = new Token(jwtToken,Token.TokenType.BEARER,false,false,user,jwtService.extraerExpiracion(jwtToken));

        tokenRepository.save(token);
    }

    private void RevokeAllUserTokens(Usuario u)
    {
        List<Token> validUserTokens = tokenRepository
                .findAllExpiradoIsFalseOrRevocadoIsFalseByuser(u.getId());
        if ( !validUserTokens.isEmpty()) {
            for ( Token token : validUserTokens) {
                token.setExpirado(true);
                token.setRevocado(true);
            }
            tokenRepository.saveAll(validUserTokens);
        }

    }

    private void RevokeToken(String token)
    {
        Token userToken = tokenRepository.findByToken(token);
        if ( userToken != null ){
            userToken.setExpirado(true);
            userToken.setRevocado(true);

            tokenRepository.save(userToken);
        }
    }


    public TokenResponse refreshToken(final String refreshToken) throws CannotCreateTokenError {

        if (refreshToken == null || refreshToken.isEmpty()){
            throw new CannotCreateTokenError("Token de refresco vacío o no presente");}

        final String userDNI = jwtService.extraerDNI(refreshToken);
        if ( userDNI == null )
            throw new CannotCreateTokenError("Token de refresco invalido");

        final Usuario usuario = userRepository.findByid(userDNI);
        if (usuario == null)
            throw new CannotCreateTokenError("Usuario no encontrado");


        if (!jwtService.isTokenValid(refreshToken,usuario))
            throw new CannotCreateTokenError("Token de refresco invalido");

        final String accessToken = jwtService.generateToken(usuario);
        final String new_refreshToken = jwtService.generateRefreshToken(usuario);
        RevokeToken(refreshToken);

        saveUserToken(usuario.getId(),new_refreshToken);

        return new TokenResponse(accessToken,new_refreshToken);

    }

    @Scheduled(cron = "0 0 4 * * ?")
    @Transactional
    public void eliminarTokensExpiradosORevocados() {
        tokenRepository.deleteTokensAntiguos();
    }

    public List<UsuarioDTO> mostrar_usuarios(){

        List<Usuario> userEntities = userRepository.findAll();
        List<UsuarioDTO> userDTOs = new ArrayList<>();

        for( Usuario u : userEntities){
            userDTOs.add(userMapper.toDTO(u));
        }

        return userDTOs;
    }
}






