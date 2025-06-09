package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.*;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.eventi.RichiestaEventoFieraDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.eventi.RichiestaEventoVisitaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.RichiestaService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.UtenteService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp.ServizioEmail;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.multipartConverter.ConvertitoreMultipartFileToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Controller che gestisce le richieste di contenuti.
 */
@Controller
@RequestMapping("/api/richieste-contenuto")
public class RichiestaController {

    @Autowired
    private final RichiestaService richiestaService;
    @Autowired
    private final ServizioEmail servizioEmail;
    @Autowired
    private final UtenteService utenteService;

    public RichiestaController(RichiestaService richiestaService, ServizioEmail servizioEmail,
            UtenteService utenteService) {
        this.richiestaService = richiestaService;
        this.servizioEmail = servizioEmail;
        this.utenteService = utenteService;
    }

    /**
     * Restituisce una richiesta di contenuto specifica in base al suo ID.
     *
     * @param id L'ID della richiesta da visualizzare.
     * @return La richiesta di contenuto con l'ID specificato, se esiste.
     */
    @GetMapping("/visualizza/{id}")
    public ResponseEntity<?> getRichiestaById(@PathVariable Long id) {
        try {

            Optional<Richiesta> richiesta = richiestaService.getRichiestaById(id);
            return richiesta.isPresent() ? ResponseEntity.ok(richiesta.get())
                    : ResponseEntity.status(404)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("{\"error\": \"Richiesta non trovata\"}");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore interno del server: " + e.getMessage());
        }
    }

    /**
     * Restituisce tutte le richieste di contenuto.
     *
     * @return Una lista di richieste di contenuto.
     */
    @GetMapping("/visualizza/all")
    public ResponseEntity<List<Richiesta>> getAllRichiesteContenuto(@RequestParam (required = false) String ordine) {
        List<Richiesta> richiesteContenuto = this.richiestaService.getAllRichiesteContenuto(ordine);
        return ResponseEntity.ok(richiesteContenuto);
    }

    /**
     * Crea una nuova richiesta di informazioni aggiuntive per un'azienda.
     *
     * @param infoAggiuntiveDTO Il DTO contenente le informazioni aggiuntive.
     * @return La richiesta creata.
     */
    @PostMapping(value = "/informazioni-aggiuntive/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> nuovaRichiestaInformazioniAggiuntive(
            @ModelAttribute @Valid RichiestaInformazioniAggiuntiveAziendaDTO infoAggiuntiveDTO) {

        try {
            File[] immagini = ConvertitoreMultipartFileToFile
                    .convertMultipartFileArrayToFileArray(infoAggiuntiveDTO.getImmagini());
            File[] certificati = ConvertitoreMultipartFileToFile
                    .convertMultipartFileArrayToFileArray(infoAggiuntiveDTO.getCertificati());

            Richiesta richiestaInfoAggiuntive = this.richiestaService.nuovaRichiestaInformazioniAggiuntive(
                    infoAggiuntiveDTO.getDescrizione(),
                    infoAggiuntiveDTO.getProduzione(),
                    infoAggiuntiveDTO.getMetodologie(),
                    immagini,
                    certificati,
                    infoAggiuntiveDTO.getAziendeCollegate());

            return ResponseEntity.ok(richiestaInfoAggiuntive);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error",
                            "Errore nella creazione della richiesta " + e.getMessage()));
        }
    }

    /**
     * Crea una nuova richiesta di prodotto.
     *
     * @param prodottoDTO Il DTO contenente le informazioni relative al prodotto.
     * @return La richiesta di prodotto creata.
     */
    @PostMapping(value = "/prodotto/singolo/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> nuovaRichiestaProdottoSingolo(
            @ModelAttribute @Valid RichiestaProdottoDTO prodottoDTO) {

        try {
            File[] immagini = ConvertitoreMultipartFileToFile
                    .convertMultipartFileArrayToFileArray(prodottoDTO.getImmagini());

            Richiesta richiestaProdotto = this.richiestaService.nuovaRichiestaProdotto(
                    prodottoDTO.getNome(),
                    prodottoDTO.getDescrizione(),
                    prodottoDTO.getIdAzienda(),
                    immagini,
                    prodottoDTO.getPrezzo(),
                    prodottoDTO.getQuantita(),
                    prodottoDTO.getAllergeni(),
                    prodottoDTO.getTecniche());

            return ResponseEntity.ok(richiestaProdotto);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error",
                            "Errore nella creazione della richiesta " + e.getMessage()));
        }
    }


    @PostMapping(value = "/prodotto/pacchetto/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> nuovaRichiestaPacchetto(
            @ModelAttribute @Valid RichiestaPacchettoDTO pacchettoDTO) {

        try {
            Richiesta richiestaPacchetto = this.richiestaService.nuovaRichiestaPacchetto(
                    pacchettoDTO.getNome(),
                    pacchettoDTO.getDescrizione(),
                    pacchettoDTO.getPrezzo(),
                    pacchettoDTO.getProdotti());

            return ResponseEntity.ok(richiestaPacchetto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error",
                            "Errore nella creazione della richiesta " + e.getMessage()));
        }

    }

    /**
     * <h2>Crea una nuova richiesta per un evento di tipo fiera.</h2>
     *
     * <p>
     * Questo metodo accetta un DTO contenente i dettagli dell'evento e crea una
     * nuova richiesta
     * utilizzando il servizio RichiestaService. Se si verifica un errore durante la
     * conversione
     * del file locandina, restituisce un errore interno del server.
     * </p>
     *
     * @param dto Il DTO contenente i dettagli dell'evento fiera.
     *            Deve includere titolo, descrizione, data di inizio e fine,
     *            locandina,
     *            indirizzo e aziende partecipanti.
     * @return Una ResponseEntity contenente la richiesta creata o un messaggio di
     *         errore.
     *         <ul>
     *         <li><b>200 OK:</b> Se la richiesta è stata creata con successo.</li>
     *         <li><b>500 Internal Server Error:</b> Se si verifica un errore
     *         durante la creazione della richiesta.</li>
     *         </ul>
     */
    @PostMapping("/fiera/new")
    public ResponseEntity<?> nuovaRichiestaFiera(@ModelAttribute @Valid RichiestaEventoFieraDTO dto) {
        try {
            File locandina = ConvertitoreMultipartFileToFile.convertiMultipartFileToFile(dto.getLocandina());

            Richiesta richiestaEvento = richiestaService.nuovaRichiestaFiera(
                    dto.getTitolo(),
                    dto.getDescrizione(),
                    dto.getInizio(),
                    dto.getFine(),
                    locandina,
                    dto.getIndirizzo(),
                    dto.getAziendePresenti());

            return ResponseEntity.ok(richiestaEvento);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error",
                            "Errore nella creazione della richiesta " + e.getMessage()));
        }
    }

    /**
     * <h2>Crea una nuova richiesta per un evento di tipo visita.</h2>
     *
     * <p>
     * Questo metodo accetta un DTO contenente i dettagli dell'evento e crea una
     * nuova richiesta
     * utilizzando il servizio <code>RichiestaService</code>. Se si verifica un
     * errore durante la conversione
     * del file locandina, restituisce un errore interno del server.
     * </p>
     *
     * @param dto Il DTO contenente i dettagli dell'evento visita.
     *            Deve includere titolo, descrizione, data di inizio e fine,
     *            locandina,
     *            indirizzo e azienda di riferimento.
     * @return Una <code>ResponseEntity</code> contenente la richiesta creata o un
     *         messaggio di errore.
     *         <ul>
     *         <li><b>200 OK:</b> Se la richiesta è stata creata con successo.</li>
     *         <li><b>500 Internal Server Error:</b> Se si verifica un errore
     *         durante la creazione della richiesta.</li>
     *         </ul>
     */
    @PostMapping("/visita/new")
    public ResponseEntity<?> nuovaRichiestaVisita(@ModelAttribute @Valid RichiestaEventoVisitaDTO dto) {
        try {
            File locandina = ConvertitoreMultipartFileToFile.convertiMultipartFileToFile(dto.getLocandina());

            Richiesta richiestaEvento = richiestaService.nuovaRichiestaVisita(
                    dto.getTitolo(),
                    dto.getDescrizione(),
                    dto.getInizio(),
                    dto.getFine(),
                    locandina,
                    dto.getIndirizzo(),
                    dto.getAziendaRiferimento());

            return ResponseEntity.ok(richiestaEvento);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error",
                            "Errore nella creazione della richiesta " + e.getMessage()));
        }
    }

    /**
     * <h4>Verifica se la richiesta non è stata ancora valutata e la fa
     * elaborare</h4>
     *
     * <p>
     * Se la richiesta è già stata valutata, restituisce un errore con un messaggio
     * appropriato.
     * Se la richiesta non è stata trovata, restituisce un errore 404.
     *
     * @param dto il DTO contenente lo stato della richiesta da valutare
     * @return una ResponseEntity contenente:
     *         - La richiesta elaborata se non è stata ancora valutata;
     *         - Un errore 400 con messaggio se la richiesta è già stata elaborata;
     *         - Un errore 404 se la richiesta non esiste.
     */
    @PatchMapping(value = "/valuta")
    public ResponseEntity<?> valutaRichiesta(@RequestBody CambiaStatoRichiestaCollaborazioneDTO dto) {
        return richiestaService.getRichiestaById(dto.getId())
                .map(richiesta -> {
                    if (richiesta.getApprovato() != null) {
                        return ResponseEntity.badRequest()
                                .body(Collections.singletonMap("message", "La richiesta è già stata elaborata"));
                    }
                    return elaborazioneRichiesta(richiesta, dto);
                })
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(Collections.singletonMap("error", "Richiesta non trovata")));
    }

    /**
     * Elabora la richiesta in base alla decisione del Curatore, che decide se
     * approvarla o rifiutarla,
     * inviando una mail all'utente che ha effettuato tale richiesta con l'esito
     * della decisione.
     *
     * @param richiesta da elaborare da parte del Curatore
     * @param dto       dell'esito della verifica della richiesta parte del Curatore
     * @return
     */
    private ResponseEntity<?> elaborazioneRichiesta(Richiesta richiesta, CambiaStatoRichiestaCollaborazioneDTO dto) {
        if (!dto.getStato()) {
            if (dto.getMessaggioAggiuntivo() == null) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("message", "Inserire un messaggio di rifiuto"));
            }
            String messaggio = "La sua richiesta di collaborazione è stata rifiutata per la seguente motivazione: "
                    + dto.getMessaggioAggiuntivo();
            Utente utente = utenteService.getUtenteById(richiesta.getIdMittente());
            String emailUtente = utente.getEmail();
            this.servizioEmail.inviaMail(emailUtente, messaggio,
                    "Valutazione Richiesta di " + richiesta.getTipologia().toString());
        }
        richiestaService.processaRichiesta(richiesta, dto.getStato());
        return ResponseEntity.ok().body(Collections.singletonMap("message",
                dto.getStato() ? "Richiesta accettata con successo." : "Richiesta correttamente rifiutata."));
    }
}
