package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.CambiaStatoRichiestaCollaborazioneDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.RichiestaCollaborazioneAziendaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.RichiestaCollaborazioneCuratoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.RichiestaCollaborazioneAnimatoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.RichiestaCollaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.RichiesteCollaborazioneService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.smtp.ServizioEmail;
import java.io.File;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.ConvertitoreMultipartFileToFile.convertiMultipartFileToFile;

@RestController
@RequestMapping("/api/richieste-collaborazione")
public class RichiestaCollaborazioneController {

    private final RichiesteCollaborazioneService richiesteCollaborazioneService;
    private final ServizioEmail servizioEmail;

    /**
     * Constructs the RichiestaCollaborazioneController with the required service
     * dependencies.
     *
     * @param richiesteCollaborazioneService the service managing collaboration
     *                                       requests
     * @param servizioEmail                  the email service for sending
     *                                       notifications
     */
    public RichiestaCollaborazioneController(RichiesteCollaborazioneService richiesteCollaborazioneService,
            ServizioEmail servizioEmail) {
        this.richiesteCollaborazioneService = richiesteCollaborazioneService;
        this.servizioEmail = servizioEmail;
    }

    /**
     * Retrieves all collaboration requests.
     *
     * @return a ResponseEntity containing the list of collaboration requests
     */
    @GetMapping
    public ResponseEntity<List<RichiestaCollaborazione>> getAllRichieste() {
        List<RichiestaCollaborazione> richieste = richiesteCollaborazioneService.getAllRichieste();
        return ResponseEntity.ok(richieste);
    }

    /**
     * Retrieves a collaboration request by its unique identifier.
     *
     * @param id the unique identifier of the collaboration request
     * @return a ResponseEntity containing the found collaboration request, or a 404
     *         status if not found, or 500 if an error occurs
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
     * Creates a new collaboration request.
     *
     * @param richiesta the RichiestaCollaborazione object to be created
     * @return a ResponseEntity containing the saved collaboration request
     */
    @PostMapping
    public ResponseEntity<RichiestaCollaborazione> createRichiesta(@RequestBody RichiestaCollaborazione richiesta) {
        RichiestaCollaborazione savedRichiesta = richiesteCollaborazioneService.saveRichiesta(richiesta);
        return ResponseEntity.ok(savedRichiesta);
    }

    /**
     * Deletes an existing collaboration request by its unique identifier.
     *
     * @param id the unique identifier of the collaboration request to delete
     * @return a ResponseEntity with no content to indicate successful deletion, or
     *         an error status if deletion fails
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
     * Creates a collaboration request for an azienda (company).
     *
     * <p>
     * This method converts the provided MultipartFiles into Files and delegates the
     * creation logic
     * to the service layer.
     * </p>
     *
     * @param richiestaAziendaDTO a DTO containing the azienda request details
     * @return a ResponseEntity containing the created collaboration request, or an
     *         error status if creation fails
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
     * Creates a collaboration request for an animatore.
     *
     * @param nome             the first name of the applicant
     * @param cognome          the last name of the applicant
     * @param telefono         the phone number of the applicant
     * @param email            the email address of the applicant
     * @param ruolo            the role of the applicant
     * @param aziendaReferente the company reference for the applicant
     * @param iban             the IBAN of the applicant
     * @param cartaIdentita    the identity card file of the applicant
     * @return a ResponseEntity containing the created collaboration request, or an
     *         error status if creation fails
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
     * Creates a collaboration request for a curatore.
     *
     * @param nome          the first name of the applicant
     * @param cognome       the last name of the applicant
     * @param telefono      the phone number of the applicant
     * @param email         the email address of the applicant
     * @param ruolo         the role of the applicant
     * @param iban          the IBAN of the applicant
     * @param cartaIdentita the identity card file of the applicant
     * @param cv            the curriculum vitae file of the applicant
     * @return a ResponseEntity containing the created collaboration request, or an
     *         error status if creation fails
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
     * Updates the status of a collaboration request.
     *
     * <p>
     * If the request is already processed, a bad request status is returned.
     * Depending on the new status,
     * the appropriate email notification is sent to the requester.
     * </p>
     *
     * @param dto an object containing the id of the collaboration request, the new
     *            status, and an optional additional message
     * @return a ResponseEntity with a success or error message
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