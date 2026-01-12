package es.esimarket.backend.config;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.LoginEncriptado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

@Configuration
public class AppConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            final Usuario user = usuarioRepository.findByid(username);
            if(user==null) throw new UsernameNotFoundException("Usuario no encontrado");

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRol()));

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getId())
                    .password(user.getContrasenna())
                    .authorities(authorities)
                    .build();
        };
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager( AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new LoginEncriptado();
    }


}
