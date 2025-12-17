package es.esimarket.backend.controllers;
import es.esimarket.backend.entities.Donaciones;
import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.exceptions.CannotCreateUserError;
import es.esimarket.backend.exceptions.CannotMakeDonationError;
import es.esimarket.backend.repositories.DonacionesRepository;
import es.esimarket.backend.repositories.UsuarioRepository;
import es.esimarket.backend.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/donations")
public class DonacionesController {

    @Autowired
    private DonacionesRepository donacionesRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PutMapping("/make/{dni}/{dineroDonado}")
    public ResponseEntity<Void> realizarDonacion( @PathVariable(name="dni") String dniObj , @PathVariable(name="dineroDonado") double dinero) throws CannotMakeDonationError {

        Usuario u = usuarioRepository.findByid(dniObj);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dniResp = auth.getName();

        UserDetails userD = userDetailsService.loadUserByUsername(dniResp);

        if ( userD.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")) )
            throw new CannotMakeDonationError("No tienes permisos para modificar las donaciones");

        if ( u == null ) throw new CannotCreateUserError("Usuario no encontrado");

        Donaciones d = donacionesRepository.findByIDUsuario(dniObj);

        if ( d.getNum() == 5 ) throw new CannotMakeDonationError("Este usuario no puede hacer mas donaciones");

        d.setNum(d.getNum() + 1);
        u.setSaldoMoneda(u.getSaldoMoneda() + ((long) Math.ceil(dinero) * 10));

        donacionesRepository.save(d);
        usuarioRepository.save(u);

        return ResponseEntity.ok().build();
    }
}
