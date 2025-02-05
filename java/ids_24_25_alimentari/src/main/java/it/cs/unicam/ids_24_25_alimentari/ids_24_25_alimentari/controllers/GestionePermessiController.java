package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.UtenteDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GestionePermessiController {
    private final UtenteService utenteService;

    public GestionePermessiController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/visualizzaUtenti")
    public ResponseEntity<List<UtenteDTO>> visualizzaUtenti() {
        return ResponseEntity.ok(utenteService.visualizzaUtenti());
    }

    public ResponseEntity<Utente> selezionaUtenteById(Long id){
        return ResponseEntity.ok(this.utenteService.selezionaUtenteById(id));
    }



}
