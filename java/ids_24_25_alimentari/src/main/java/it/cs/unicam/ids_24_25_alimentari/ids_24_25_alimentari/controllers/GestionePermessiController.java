package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.UtenteDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.RichiestaCollaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.GestionePermessiService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/admin")
public class GestionePermessiController {
    private final UtenteService utenteService;
    private final GestionePermessiService gestionePermessiService;

    public GestionePermessiController(UtenteService utenteService, GestionePermessiService gestionePermessiService) {
        this.utenteService = utenteService;
        this.gestionePermessiService = gestionePermessiService;
    }

    @GetMapping("/visualizzaUtenti")
    public ResponseEntity<List<UtenteDTO>> visualizzaUtenti() {
        return ResponseEntity.ok(utenteService.visualizzaUtenti());
    }

    @GetMapping("/selezionaUtente")  //aggiunta metodo per selezionare un utente in base all'id  //aggiunta metodo per selezionare un utente in base all'id  //aggiunta metodo per selezionare un utente in base all'id  //aggiunta metodo per selezionare un utente in base all'id  //aggiunta metodo per selezionare un utente in base all
    public ResponseEntity<Optional<Utente>> selezionaUtenteById(@RequestParam long id){
        return ResponseEntity.ok(this.utenteService.selezionaUtenteById(id));
    }
    @PostMapping("/accettaRichiesta")
    public ResponseEntity <RichiestaCollaborazione> accettaRichiesta(@RequestBody String idRichiesta){
        return ResponseEntity.ok(this.gestionePermessiService.AccettaRichiestaCollaborazione((Long.parseLong(idRichiesta))));
    }
}
