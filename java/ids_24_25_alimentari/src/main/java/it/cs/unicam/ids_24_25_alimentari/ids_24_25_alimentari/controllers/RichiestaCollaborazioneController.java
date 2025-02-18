package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.CambiaStatoRichiestaCollaborazioneDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.RichiestaCollaborazioneAziendaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.RichiestaCollaborazioneCuratoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.RichiestaCollaborazioneAnimatoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.RichiesteCollaborazione.RichiestaCollaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.RichiesteCollaborazioneService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.smtp.ServizioEmail;
import java.io.File;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.ConvertitoreMultipartFileToFile.convertiMultipartFileToFile;

@RestController
@RequestMapping("/api/richieste-collaborazione")
public class RichiestaCollaborazioneController {

    private final RichiesteCollaborazioneService richiesteCollaborazioneService;
    private final ServizioEmail servizioEmail;


    public RichiestaCollaborazioneController(RichiesteCollaborazioneService richiesteCollaborazioneService,
            ServizioEmail servizioEmail) {
        this.richiesteCollaborazioneService = richiesteCollaborazioneService;
        this.servizioEmail = servizioEmail;
    }

    /**
     * <h2>Recupera tutte le richieste di collaborazione</h2>
     * <br>
     * Questo metodo restituisce tutte le richieste di collaborazione presenti nel sistema.
     *
     * @return {@code ResponseEntity} contenente la lista di {@code RichiestaCollaborazione}.
     */
    @GetMapping
    public ResponseEntity<List<RichiestaCollaborazione>> getAllRichieste() {
        List<RichiestaCollaborazione> richieste = richiesteCollaborazioneService.getAllRichieste();
        return ResponseEntity.ok(richieste);
    }

    /**
     * <h2>Recupera una richiesta di collaborazione per ID</h2>
     * <br>
     * Questo metodo cerca una richiesta di collaborazione specifica in base all'ID fornito.
     * Se la richiesta esiste, viene restituita con stato HTTP 200 (OK).
     * Se la richiesta non viene trovata, restituisce uno stato HTTP 404 (NOT FOUND)
     * con un messaggio di errore in formato JSON.
     * In caso di errore imprevisto, restituisce uno stato HTTP 500 (INTERNAL SERVER ERROR).
     *
     * @param id L'ID della richiesta di collaborazione da recuperare.
     * @return {@code ResponseEntity} contenente la richiesta trovata o un messaggio di errore.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRichiestaById(@PathVariable Long id) {
        try {
            Optional<RichiestaCollaborazione> richiesta = richiesteCollaborazioneService.getRichiestaById(id);
            if (richiesta.isPresent()) {
                return ResponseEntity.ok(richiesta.get());
            } else {
                return ResponseEntity.status(404)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\": \"Richiesta non trovata\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore interno del server: " + e.getMessage());
        }
    }

    /**
     * <h2>Elimina una richiesta di collaborazione</h2>
     * <br>
     * Questo metodo permette di eliminare una richiesta di collaborazione specifica
     * in base all'ID fornito.
     * Se la richiesta esiste, viene eliminata e viene restituito un messaggio di conferma
     * con stato HTTP 200 (OK).
     * Se la richiesta non viene trovata, restituisce uno stato HTTP 404 (NOT FOUND)
     * con un messaggio di errore in formato JSON.
     * In caso di errore durante l'eliminazione, restituisce uno stato HTTP 500 (INTERNAL SERVER ERROR).
     *
     * @param id L'ID della richiesta di collaborazione da eliminare.
     * @return {@code ResponseEntity} contenente un messaggio di conferma o di errore.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRichiesta(@PathVariable Long id) {
        try {
            Optional<RichiestaCollaborazione> richiesta = richiesteCollaborazioneService.getRichiestaById(id);
            if (richiesta.isPresent()) {
                richiesteCollaborazioneService.deleteRichiesta(id);
                return ResponseEntity.ok(Collections.singletonMap("message", "Richiesta eliminata con successo"));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("error", "Richiesta non trovata"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error",
                            "Errore nella cancellazione della richiesta: " + e.getMessage()));
        }
    }

    /**
     * <h2>Crea una richiesta di collaborazione per un'azienda</h2>
     * <br>
     * Questo metodo permette di creare una richiesta di collaborazione per un'azienda,
     * accettando i dati sotto forma di `multipart/form-data`.
     * I file allegati (certificato e carta d'identità) vengono convertiti e inclusi nella richiesta.
     *
     * @param richiestaAziendaDTO L'oggetto {@code RichiestaCollaborazioneAziendaDTO} contenente
     *                             i dati dell'azienda e i file allegati.
     * @return {@code ResponseEntity} contenente la richiesta creata con stato HTTP 200 (OK)
     *         o un messaggio di errore con stato HTTP 500 (INTERNAL SERVER ERROR).
     */
    @PostMapping(value = "/azienda", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> creaRichiestaAzienda(
            @ModelAttribute @Valid RichiestaCollaborazioneAziendaDTO richiestaAziendaDTO) {

        try {
            // Convert MultipartFiles to File objects.
            File fileCertificato = convertiMultipartFileToFile(richiestaAziendaDTO.getCertificato());
            File fileCartaIdentita = convertiMultipartFileToFile(richiestaAziendaDTO.getCartaIdentita());

            // Create the collaboration request.
            RichiestaCollaborazione richiesta = richiesteCollaborazioneService.creaRichiestaAzienda(
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
                    .body(Collections.singletonMap("error",
                            "Errore nella creazione della richiesta: " + e.getMessage()));
        }
    }
    /**
     * <h2>Crea una richiesta di collaborazione per un animatore</h2>
     * <br>
     * Questo metodo permette di creare una richiesta di collaborazione per un animatore,
     * accettando i dati sotto forma di `multipart/form-data`.
     * Il file allegato (carta d'identità) viene convertito e incluso nella richiesta.
     *
     * @param richiestaAnimatoreDTO L'oggetto {@code RichiestaCollaborazioneAnimatoreDTO} contenente
     *                               i dati dell'animatore e il file della carta d'identità.
     * @return {@code ResponseEntity} contenente la richiesta creata con stato HTTP 200 (OK)
     *         o un messaggio di errore con stato HTTP 500 (INTERNAL SERVER ERROR).
     */
    @PostMapping(value = "/animatore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> creaRichiestaAnimatore(
            @ModelAttribute RichiestaCollaborazioneAnimatoreDTO richiestaAnimatoreDTO) {

        try {
            // Convert the MultipartFile to a File object.
            File fileCartaIdentita = convertiMultipartFileToFile(richiestaAnimatoreDTO.getCartaIdentita());

            // Create the collaboration request.
            RichiestaCollaborazione richiesta = richiesteCollaborazioneService.creaRichiestaAnimatore(
                    richiestaAnimatoreDTO.getNome(),
                    richiestaAnimatoreDTO.getCognome(),
                    richiestaAnimatoreDTO.getTelefono(),
                    richiestaAnimatoreDTO.getEmail(),
                    richiestaAnimatoreDTO.getRuolo(),
                    richiestaAnimatoreDTO.getAziendaReferente(),
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
     * Questo metodo permette di creare una richiesta di collaborazione per un curatore,
     * accettando i dati sotto forma di `multipart/form-data`.
     * I file allegati (carta d'identità e curriculum vitae) vengono convertiti e inclusi nella richiesta.
     *
     * @param richiestaCuratoreDTO L'oggetto {@code RichiestaCollaborazioneCuratoreDTO} contenente
     *                              i dati del curatore e i file allegati.
     * @return {@code ResponseEntity} contenente la richiesta creata con stato HTTP 200 (OK)
     *         o un messaggio di errore con stato HTTP 500 (INTERNAL SERVER ERROR).
     */
    @PostMapping(value = "/curatore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> creaRichiestaCuratore(
            @ModelAttribute RichiestaCollaborazioneCuratoreDTO richiestaCuratoreDTO) {

        try {
            // Convert MultipartFiles to File objects.
            File fileCartaIdentita = convertiMultipartFileToFile(richiestaCuratoreDTO.getCartaIdentita());
            File fileCv = convertiMultipartFileToFile(richiestaCuratoreDTO.getCv());

            // Create the collaboration request.
            RichiestaCollaborazione richiesta = richiesteCollaborazioneService.creaRichiestaCuratore(
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
     * Questo metodo permette di accettare o rifiutare una richiesta di collaborazione.
     * Se la richiesta è già stata elaborata, restituisce un errore.
     * In caso di rifiuto, è necessario fornire un messaggio di motivazione.
     * Se la richiesta viene rifiutata, viene inviata una mail di notifica all'utente.
     *
     * @param dto L'oggetto {@code CambiaStatoRichiestaCollaborazioneDTO} contenente
     *            l'ID della richiesta e il nuovo stato da impostare.
     * @return {@code ResponseEntity} con un messaggio di conferma o errore:
     *         - HTTP 200 (OK) se lo stato è stato aggiornato correttamente.
     *         - HTTP 400 (BAD REQUEST) se la richiesta è già stata elaborata
     *           o se manca il messaggio in caso di rifiuto.
     *         - HTTP 404 (NOT FOUND) se la richiesta non esiste.
     */
    @PatchMapping("/stato")
    public ResponseEntity<?> updateStato(@RequestBody CambiaStatoRichiestaCollaborazioneDTO dto) {

        Optional<RichiestaCollaborazione> richiesta = richiesteCollaborazioneService.getRichiestaById(dto.getId());
        if (richiesta.isPresent()) {
            if (richiesta.get().getStato() != null) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("message", "La richiesta è già stata elaborata"));
            }

            if (dto.getStato()) {
                richiesteCollaborazioneService.setStato(dto.getId(), dto.getStato());
            } else {
                if (dto.getMessaggioAggiuntivo() == null) {
                    return ResponseEntity.badRequest()
                            .body(Collections.singletonMap("message", "Inserire un messaggio di rifiuto"));
                }
                String messaggio = "La sua richiesta di collaborazione è stata rifiutata per la seguente motivazione: "
                        + dto.getMessaggioAggiuntivo();
                this.servizioEmail.inviaMail(richiesta.get().getEmail(), messaggio,
                        "Accettazione Richiesta di Collaborazione");
                richiesteCollaborazioneService.setStato(dto.getId(), dto.getStato());
            }
            return ResponseEntity.ok().body(Collections.singletonMap("message",
                    dto.getStato() ? "Richiesta accettata con successo." : "Richiesta correttamente rifiutata."));
        }
        return ResponseEntity.status(404).body(Collections.singletonMap("error", "Richiesta non trovata"));
    }
}