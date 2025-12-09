package es.esimarket.backend.controllers;
import es.esimarket.backend.repositories.DonacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/donaciones")
public class DonacionesController {

    @Autowired
    private DonacionesRepository donacionesRepository;

    @PostMapping("/realizar")
    public ResponseEntity<Void> realizarDonacion(){
        return ResponseEntity.ok().build();
    }
}
