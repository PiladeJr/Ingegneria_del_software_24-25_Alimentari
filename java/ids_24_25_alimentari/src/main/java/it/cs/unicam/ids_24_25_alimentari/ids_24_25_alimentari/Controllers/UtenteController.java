package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Models.Utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UtenteController {
    private final UtenteService utenteService;
    @Autowired
    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }
    @GetMapping("/visualizzaUtenti")
    public ResponseEntity<List<Utente>> visualizzaUtenti(){
        return ResponseEntity.ok(utenteService.visualizzaUtenti());
    }

}
