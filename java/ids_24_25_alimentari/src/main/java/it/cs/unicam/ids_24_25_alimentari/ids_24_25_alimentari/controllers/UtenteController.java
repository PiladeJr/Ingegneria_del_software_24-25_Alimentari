package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.UtenteDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UtenteController {
    private final UtenteService utenteService;
    @Autowired
    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }
    @GetMapping("/visualizzaUtenti")
    public ResponseEntity<List<UtenteDTO>> visualizzaUtenti(){
        return ResponseEntity.ok(utenteService.visualizzaUtenti());
    }
    @PostMapping("/registrazione")
    public ResponseEntity<String> registraUtente(
            @RequestParam String nome,
            @RequestParam String cognome,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String telefono
            ){
        try{
            UtenteDTO utente = new UtenteDTO(nome, cognome, email, password, telefono);
            utenteService.registraUtente(utente);
            return new ResponseEntity<>("Utente registrato correttamente", HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace(); // Log dettagliato per identificare il problema
            return new ResponseEntity<>("Errore interno del server: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
