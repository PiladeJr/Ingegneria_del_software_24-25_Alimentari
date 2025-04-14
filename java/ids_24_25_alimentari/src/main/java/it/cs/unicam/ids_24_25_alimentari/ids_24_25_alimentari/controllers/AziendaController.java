package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.Richieste.RichiestaInformazioniAggiuntiveAziendaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.AziendaService;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.RichiestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.ConvertitoreMultipartFileToFile.convertMultipartFileArrayToFileArray;

/**
 * Controller per la gestione delle operazioni relative all'entità Azienda.
 * Fornisce endpoint per ottenere, salvare, eliminare e creare entità Azienda.
 */
@RestController
@RequestMapping("/api/azienda")
public class AziendaController {

    @Autowired
    private final AziendaService aziendaService;
    @Autowired
    private final RichiestaService richiestaService;

    public AziendaController(AziendaService aziendaService, RichiestaService richiestaService) {
        this.aziendaService = aziendaService;
        this.richiestaService = richiestaService;
    }

    /**
     * restituisce una lista di tutte le aziende salvate dalla piattaforma
     *
     * @return ResponseEntity contenente la lista di tutte le aziende.
     */
    @GetMapping
    public ResponseEntity<List<Azienda>> getAllAziende() {
        List<Azienda> aziende = aziendaService.getAllAziende();
        return ResponseEntity.ok(aziende);
    }

    /**
     * ottiene un'azienda dal suo id.
     *
     * @param id l'ID della Azienda.
     * @return ResponseEntity contenente l'azienda se trovata, oppure un 404 status
     *         se non trovata, oppure un errore 500 in caso di eccezione.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAziendaById(@PathVariable Long id) {
        try {
            return aziendaService.getAziendaById(id)
                    .map(azienda -> ResponseEntity.<Object>ok(azienda))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Collections.singletonMap("error", "Azienda non trovata")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Errore interno del server: " + e.getMessage()));
        }
    }

    /**
     * ottiene la lista di aziende selezionate in base al filtro
     * ruolo dell'utente
     *
     * @param ruolo il ruolo dell'utente in base a cui filtrare le aziende
     * @return la lista di aziende appartenenti ad utenti con il ruolo specificato
     */
    @GetMapping("/roles/{ruolo}")
    public ResponseEntity<?> getAziendeByRuolo(@PathVariable String ruolo) {
        try {
            Ruolo ruoloEnum = Ruolo.valueOf(ruolo.toUpperCase());
            List<Azienda> aziende = aziendaService.getAziendeByRuolo(ruoloEnum);

            if (aziende == null || aziende.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Nessuna azienda trovata per il ruolo specificato"));
            }

            return ResponseEntity.ok(aziende);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Ruolo non valido: " + ruolo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Errore interno del server: " + e.getMessage()));
        }
    }

    /**
     * Elimina un'entità {@link Azienda} tramite il suo ID
     *
     * @param id dell'azienda che vogliamo eliminare.
     * @return ResponseEntity con un body JSON contenente un messaggio d'errore se l'eliminazione
     *         fallisce, altrimenti nessun contenuto.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAzienda(@PathVariable Long id) {
        try {
            Optional<Azienda> azienda = aziendaService.getAziendaById(id);
            if (azienda.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Azienda non trovata"));
            } else {
                aziendaService.deleteAzienda(id);
                return ResponseEntity.status(200)
                        .body(Collections.singletonMap("message", "Azienda eliminata correttamente"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "ID non valido: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Errore interno del server: " + e.getMessage()));
        }
    }

    /**
     * Crea una nuova Richiesta di informazioni di tipo InfoAzienda.
     * Questo endpoint si aspetta una serie di parametri in formato
     * multipart/form-data per creare una nuova Richiesta di informazioni di tipo
     * InfoAzienda.
     *
     * @param richiestaInformazioniAggiuntiveAziendaDTO
     * @return ResponseEntity contenente la Richiesta creata.
     */
    @PostMapping(value = "/informazioni/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAziendaInformazioni(
            @ModelAttribute RichiestaInformazioniAggiuntiveAziendaDTO richiestaInformazioniAggiuntiveAziendaDTO) {

        File[] immaginiFiles;
        try {
            immaginiFiles = convertMultipartFileArrayToFileArray(
                    richiestaInformazioniAggiuntiveAziendaDTO.getImmagini());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error",
                            "Errore durante la conversione delle immagini: " + e.getMessage()));
        }

        File[] certificatiFiles;
        try {
            certificatiFiles = convertMultipartFileArrayToFileArray(
                    richiestaInformazioniAggiuntiveAziendaDTO.getCertificati());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error",
                            "Errore durante la conversione dei certificati: " + e.getMessage()));
        }

        try {
            Richiesta richiesta = this.richiestaService.nuovaRichiestaInformazioniAggiuntive(
                    richiestaInformazioniAggiuntiveAziendaDTO.getDescrizione(),
                    richiestaInformazioniAggiuntiveAziendaDTO.getProduzione(),
                    richiestaInformazioniAggiuntiveAziendaDTO.getMetodologie(),
                    immaginiFiles,
                    certificatiFiles,
                    richiestaInformazioniAggiuntiveAziendaDTO.getAziendeCollegate());
            return ResponseEntity.ok(richiesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Parametro non valido: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Errore interno del server: " + e.getMessage()));
        }
    }
}