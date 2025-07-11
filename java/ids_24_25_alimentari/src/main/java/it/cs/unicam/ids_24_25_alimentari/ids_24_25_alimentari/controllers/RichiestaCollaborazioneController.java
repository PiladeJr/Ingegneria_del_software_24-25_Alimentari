package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import javax.validation.Valid;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione.RichiesteCollaborazioneOutDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.RichiestaCollaborazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.ValutaRichiestaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione.RichiestaCollaborazioneAziendaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione.RichiestaCollaborazioneCuratoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione.RichiestaCollaborazioneAnimatoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Collaborazione.RichiesteCollaborazioneService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp.ServizioEmail;
import java.io.File;

import java.util.List;
import java.util.Collections;

import static it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.multipartConverter.ConvertitoreMultipartFileToFile.convertiMultipartFileToFile;

@RestController
@RequestMapping("/api/richieste-collaborazione")
public class RichiestaCollaborazioneController {
    @Autowired
    private final RichiesteCollaborazioneService richiesteCollaborazioneService;
    @Autowired
    private final ServizioEmail servizioEmail;

    public RichiestaCollaborazioneController(RichiesteCollaborazioneService richiesteCollaborazioneService,
            ServizioEmail servizioEmail) {
        this.richiesteCollaborazioneService = richiesteCollaborazioneService;
        this.servizioEmail = servizioEmail;
    }

    /**
     * <h2>Recupera tutte le richieste di collaborazione</h2>
     * <br>
     * Questo metodo restituisce tutte le richieste di collaborazione presenti nel
     * sistema.
     *
     * @return {@code ResponseEntity} contenente la lista di
     *         {@code RichiestaCollaborazione}.
     */
    @GetMapping
    public ResponseEntity<List<RichiesteCollaborazioneOutDTO>> getAllRichieste(
            @RequestParam(required = false) String ordine) {
        List<RichiesteCollaborazioneOutDTO> richieste = richiesteCollaborazioneService.getAllRichieste(ordine);
        return ResponseEntity.ok(richieste);
    }

    /**
     * <h2>Recupera una richiesta di collaborazione per ID</h2>
     * <br>
     * Questo metodo cerca una richiesta di collaborazione specifica in base all'ID
     * fornito.
     * Se la richiesta esiste, viene restituita con stato HTTP 200 (OK).
     * Se la richiesta non viene trovata, restituisce uno stato HTTP 404 (NOT FOUND)
     * con un messaggio di errore in formato JSON.
     * In caso di errore imprevisto, restituisce uno stato HTTP 500 (INTERNAL SERVER
     * ERROR).
     *
     * @param id L'ID della richiesta di collaborazione da recuperare.
     * @return {@code ResponseEntity} contenente la richiesta trovata o un messaggio
     *         di errore.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRichiestaById(@PathVariable Long id) {
        try {
            return richiesteCollaborazioneService.getCollaborazioneById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore interno del server: " + e.getMessage());
        }
    }

/**
 * <h2>Elimina una richiesta di collaborazione virtualmente</h2>
 * <br>
 * Questo metodo consente di eliminare una richiesta di collaborazione specifica
 * impostando il suo stato come "ELIMINATO" in modo virtuale, senza rimuoverla dal database.
 * <br>
 * Se la richiesta esiste, viene aggiornata e restituito un messaggio di conferma
 * con stato HTTP 200 (OK).
 * <br>
 * Se la richiesta non viene trovata, restituisce uno stato HTTP 404 (NOT FOUND)
 * con un messaggio di errore in formato JSON.
 * <br>
 * In caso di errore durante l'eliminazione, restituisce uno stato HTTP 500
 * (INTERNAL SERVER ERROR).
 *
 * @param id L'ID della richiesta di collaborazione da eliminare virtualmente.
 * @return {@code ResponseEntity} contenente un messaggio di conferma o di errore.
 */
@DeleteMapping("/{id}/elimina")
public ResponseEntity<?> eliminaRichiestaVirtuale(@PathVariable Long id) {
    try {
        return richiesteCollaborazioneService.deleteRichiestaVirtuale(id);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error",
                        "Errore nella cancellazione virtuale della richiesta: " + e.getMessage()));
    }
}

    /**
     * <h2>Elimina una richiesta di collaborazione</h2>
     * <br>
     * Questo metodo permette di eliminare una richiesta di collaborazione
     * specifica
     * in base all'ID fornito.
     * Se la richiesta esiste, viene eliminata e viene restituito un messaggio di
     * conferma
     * con stato HTTP 200 (OK).
     * Se la richiesta non viene trovata, restituisce uno stato HTTP 404 (NOT
     * FOUND)
     * con un messaggio di errore in formato JSON.
     * In caso di errore durante l'eliminazione, restituisce uno stato HTTP 500
     * (INTERNAL SERVER ERROR).
     *
     * @param id L'ID della richiesta di collaborazione da eliminare.
     * @return {@code ResponseEntity} contenente un messaggio di conferma o di
     * errore.
     */
     @DeleteMapping("/{id}/elimina-fisica")
     public ResponseEntity<?> eliminaRichiestaFisica(@PathVariable Long id) {
         try {
             return richiesteCollaborazioneService.deleteRichiestaFisica(id);
         } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body(Collections.singletonMap("error",
                             "Errore nella cancellazione della richiesta: " + e.getMessage()));
         }
     }

     /**
     * <h2>Crea una richiesta di collaborazione per un'azienda</h2>
     * <br>
     * Questo metodo permette di creare una richiesta di collaborazione per
     * un'azienda,
     * accettando i dati sotto forma di `multipart/form-data`.
     * I file allegati (certificato e carta d'identità) vengono convertiti e
     * inclusi nella richiesta.
     *
     * @param richiestaAziendaDTO L'oggetto {@code
     * RichiestaCollaborazioneAziendaDTO} contenente
     * i dati dell'azienda e i file allegati.
     * @return {@code ResponseEntity} contenente la richiesta creata con stato
     * HTTP 200 (OK)
     * o un messaggio di errore con stato HTTP 500 (INTERNAL SERVER ERROR).
     */
     @PostMapping(value = "/azienda", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     public ResponseEntity<?> creaRichiestaAzienda(@ModelAttribute @Valid RichiestaCollaborazioneAziendaDTO richiestaAziendaDTO) {
         try {
             // Convert MultipartFiles to File objects.
             File fileCertificato =
             convertiMultipartFileToFile(richiestaAziendaDTO.getCertificato());
             File fileCartaIdentita =
             convertiMultipartFileToFile(richiestaAziendaDTO.getCartaIdentita());

             RichiestaCollaborazione richiesta =
             richiesteCollaborazioneService.creaRichiestaAzienda(
             richiestaAziendaDTO.getNome(),
             richiestaAziendaDTO.getCognome(),
             richiestaAziendaDTO.getTelefono(),
             richiestaAziendaDTO.getEmail(),
             richiestaAziendaDTO.getRuolo(),
             richiestaAziendaDTO.getDenSociale(),
             richiestaAziendaDTO.getSedeLegale(),
             richiestaAziendaDTO.getSedeOperativa(),
             richiestaAziendaDTO.getIban(),
             richiestaAziendaDTO.getIva(),
             fileCertificato,
             fileCartaIdentita);

             return ResponseEntity.ok(richiesta);
         } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Errore nella creazione della richiesta: " + e.getMessage()));
             }
     }

    /**
     * <h2>Crea una richiesta di collaborazione per un animatore</h2>
     * <br>
     * Questo metodo permette di creare una richiesta di collaborazione per un
     * animatore,
     * accettando i dati sotto forma di `multipart/form-data`.
     * Il file allegato (carta d'identità) viene convertito e incluso nella
     * richiesta.
     *
     * @param richiestaAnimatoreDTO L'oggetto {@code
     *                              RichiestaCollaborazioneAnimatoreDTO} contenente
     *                              i dati dell'animatore e il file della carta d'identità.
     * @return {@code ResponseEntity} contenente la richiesta creata con stato
     * HTTP 200 (OK)
     * o un messaggio di errore con stato HTTP 500 (INTERNAL SERVER ERROR).
     */
    @PostMapping(value = "/animatore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> creaRichiestaAnimatore(
            @ModelAttribute RichiestaCollaborazioneAnimatoreDTO richiestaAnimatoreDTO) {

        try {
            File fileCartaIdentita =
                    convertiMultipartFileToFile(richiestaAnimatoreDTO.getCartaIdentita());

            RichiestaCollaborazione richiesta =
                    richiesteCollaborazioneService.creaRichiestaAnimatore(
                            richiestaAnimatoreDTO.getNome(),
                            richiestaAnimatoreDTO.getCognome(),
                            richiestaAnimatoreDTO.getTelefono(),
                            richiestaAnimatoreDTO.getEmail(),
                            richiestaAnimatoreDTO.getRuolo(),
                            richiestaAnimatoreDTO.getIban(),
                            fileCartaIdentita);

            return ResponseEntity.ok(richiesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error",
                            "Errore nella creazione della richiesta: " + e.getMessage()));
        }
    }

    /**
     * <h2>Crea una richiesta di collaborazione per un curatore</h2>
     * <br>
     * Questo metodo permette di creare una richiesta di collaborazione per un
     * curatore,
     * accettando i dati sotto forma di `multipart/form-data`.
     * I file allegati (carta d'identità e curriculum vitae) vengono convertiti e
     * inclusi nella richiesta.
     *
     * @param richiestaCuratoreDTO L'oggetto {@code
     *                             RichiestaCollaborazioneCuratoreDTO} contenente
     *                             i dati del curatore e i file allegati.
     * @return {@code ResponseEntity} contenente la richiesta creata con stato
     * HTTP 200 (OK)
     * o un messaggio di errore con stato HTTP 500 (INTERNAL SERVER ERROR).
     */
    @PostMapping(value = "/curatore", consumes =
            MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> creaRichiestaCuratore(
            @ModelAttribute RichiestaCollaborazioneCuratoreDTO richiestaCuratoreDTO) {

        try {
            File fileCartaIdentita =
                    convertiMultipartFileToFile(richiestaCuratoreDTO.getCartaIdentita());
            File fileCv = convertiMultipartFileToFile(richiestaCuratoreDTO.getCv());

            RichiestaCollaborazione richiesta =
                    richiesteCollaborazioneService.creaRichiestaCuratore(
                            richiestaCuratoreDTO.getNome(),
                            richiestaCuratoreDTO.getCognome(),
                            richiestaCuratoreDTO.getTelefono(),
                            richiestaCuratoreDTO.getEmail(),
                            richiestaCuratoreDTO.getRuolo(),
                            richiestaCuratoreDTO.getIban(),
                            fileCartaIdentita,
                            fileCv);

            return ResponseEntity.ok(richiesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error",
                            "Errore nella creazione della richiesta: " + e.getMessage()));
        }
    }

    /**
     * <h2>Aggiorna lo stato di una richiesta di collaborazione</h2>
     * <br>
     * Questo metodo consente di accettare o rifiutare una richiesta di collaborazione.
     * Se la richiesta è già stata elaborata, restituisce un errore.
     * In caso di rifiuto, è necessario fornire un messaggio di motivazione.
     * Una volta elaborata, viene inviata una mail di notifica all'utente.
     *
     * @param dto L'oggetto {@code ValutaRichiestaDTO} contenente lo stato da impostare
     *            e un eventuale messaggio di motivazione in caso di rifiuto.
     * @param id  L'ID della richiesta di collaborazione da aggiornare.
     * @return {@code ResponseEntity} con un messaggio di conferma o errore:
     *         - HTTP 200 (OK) se lo stato è stato aggiornato correttamente.
     *         - HTTP 400 (BAD REQUEST) se la richiesta è già stata elaborata
     *           o se manca il messaggio in caso di rifiuto.
     *         - HTTP 404 (NOT FOUND) se la richiesta non esiste.
     */
    @PatchMapping("/{id}/stato")
    public ResponseEntity<?> elaboraRichiesta(@RequestBody ValutaRichiestaDTO dto, @PathVariable Long id) {
        try {
            return richiesteCollaborazioneService.elaboraRichiesta(dto, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}