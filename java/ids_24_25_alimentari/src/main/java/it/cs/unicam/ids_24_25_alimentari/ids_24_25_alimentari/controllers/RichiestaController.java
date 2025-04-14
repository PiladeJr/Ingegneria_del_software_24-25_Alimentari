package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.*;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiesta.*;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.RichiestaService;
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

    public RichiestaController(RichiestaService richiestaService, ServizioEmail servizioEmail, UtenteService utenteService) {
        this.richiestaService = richiestaService;
        this.servizioEmail = servizioEmail;
        this.utenteService = utenteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRichiestaById(@PathVariable Long id) {
        try {

            Optional<Richiesta> richiesta = richiestaService.getRichiestaById(id);
            return richiesta.isPresent() ? ResponseEntity.ok(richiesta.get()) :
                    ResponseEntity.status(404)
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
    @GetMapping
    public ResponseEntity<List<Richiesta>> getAllRichiesteContenuto(){
        List<Richiesta> richiesteContenuto = this.richiestaService.getAllRichiesteContenuto();
        return ResponseEntity.ok(richiesteContenuto);
    }

    /**
     * Restituisce tutte le richieste filtrate per tipologia.
     *
     * @param tipologia La tipologia della richiesta da filtrare.
     * @return Una lista di richieste filtrate per tipologia.
     */
    @GetMapping("/{tipologia}")
    public ResponseEntity<List<Richiesta>> getAllRichiesteByTipo(@PathVariable Tipologia tipologia){
        List<Richiesta> richiesteByTipo = this.richiestaService.getRichiesteByTipo(tipologia);
        return ResponseEntity.ok(richiesteByTipo);
    }

    /**
     * Crea una nuova richiesta di informazioni aggiuntive per un'azienda.
     *
     * @param infoAggiuntiveDTO Il DTO contenente le informazioni aggiuntive.
     * @return La richiesta creata.
     */
    @PostMapping(value = "/informazioni-aggiuntive/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> nuovaRichiestaInformazioniAggiuntive(
            @ModelAttribute @Valid RichiestaInformazioniAggiuntiveAziendaDTO infoAggiuntiveDTO){

        try {
            File[] immagini = ConvertitoreMultipartFileToFile.convertMultipartFileArrayToFileArray(infoAggiuntiveDTO.getImmagini());
            File[] certificati = ConvertitoreMultipartFileToFile.convertMultipartFileArrayToFileArray(infoAggiuntiveDTO.getCertificati());

            Richiesta richiestaInfoAggiuntive = this.richiestaService.nuovaRichiestaInformazioniAggiuntive(
                    infoAggiuntiveDTO.getDescrizione(),
                    infoAggiuntiveDTO.getProduzione(),
                    infoAggiuntiveDTO.getMetodologie(),
                    immagini,
                    certificati,
                    infoAggiuntiveDTO.getAziendeCollegate()
            );

            return ResponseEntity.ok(richiestaInfoAggiuntive);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error",
                            "Errore nella creazione della richiesta "+e.getMessage()));
        }
    }

    /**
     * Crea una nuova richiesta di prodotto.
     *
     * @param prodottoDTO Il DTO contenente le informazioni relative al prodotto.
     * @return La richiesta di prodotto creata.
     */
    @PostMapping(value = "/prodotto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> nuovaRichiestaProdotto(
            @ModelAttribute @Valid RichiestaProdottoDTO prodottoDTO
    ){

        try {
            File[] immagini = ConvertitoreMultipartFileToFile.convertMultipartFileArrayToFileArray(prodottoDTO.getImmagini());

            Richiesta richiestaProdotto = this.richiestaService.nuovaRichiestaProdotto(
                    prodottoDTO.getNome(),
                    prodottoDTO.getDescrizione(),
                    prodottoDTO.getIdAzienda(),
                    immagini,
                    prodottoDTO.getPrezzo(),
                    prodottoDTO.getQuantita(),
                    prodottoDTO.getAllergeni(),
                    prodottoDTO.getTecniche()
            );

            return ResponseEntity.ok(richiestaProdotto);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error",
                            "Errore nella creazione della richiesta "+e.getMessage()));
        }
    }


    @PostMapping(value = "/pacchetto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> nuovaRichiestaPacchetto(
            @ModelAttribute @Valid RichiestaPacchettoDTO pacchettoDTO
    ){

        Richiesta richiestaPacchetto = this.richiestaService.nuovaRichiestaPacchetto(
                pacchettoDTO.getNome(),
                pacchettoDTO.getDescrizione(),
                pacchettoDTO.getPrezzo(),
                pacchettoDTO.getProdotti()
        );

        return ResponseEntity.ok(richiestaPacchetto);

    }


    /**
     * Verifica se la richiesta non è stata ancora valutata e la fa elaborare
     *
     * <p>Se la richiesta è già stata valutata, restituisce un errore con un messaggio appropriato.
     * Se la richiesta non è stata trovata, restituisce un errore 404.
     *
     * @param dto il DTO contenente lo stato della richiesta da valutare
     * @return una ResponseEntity contenente:
     *         - La richiesta elaborata se non è stata ancora valutata;
     *         - Un errore 400 con messaggio se la richiesta è già stata elaborata;
     *         - Un errore 404 se la richiesta non esiste.
     */
    @PatchMapping(value = "/stato")
    public ResponseEntity<?> valutaRichiesta(@RequestBody CambiaStatoRichiestaCollaborazioneDTO dto){
        return richiestaService.getRichiestaById(dto.getId())
                .map(richiesta -> {
                    if (richiesta.isApprovato() != null) {
                        return ResponseEntity.badRequest()
                                .body(Collections.singletonMap("message", "La richiesta è già stata elaborata"));
                    }
                    return elaborazioneRichiesta(richiesta, dto);
                })
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(Collections.singletonMap("error", "Richiesta non trovata")));


        //            Optional<Richiesta> richiesta = richiestaService.getRichiestaById(dto.getId());
//
//            if(richiesta.isPresent()) {
//                if (richiesta.get().isApprovato() != null) {
//                    return ResponseEntity.badRequest()
//                            .body(Collections.singletonMap("message", "La richiesta è già stata elaborata"));
//                } else {
//                    return elaborazioneRichiesta(richiesta.get(), dto);
//                }
//            }
//            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Richiesta non trovata"));


    }


    /**
     * Elabora la richiesta in base alla decisione del Curatore, che decide se approvarla o rifiutarla,
     * inviando una mail all'utente che ha effettuato tale richiesta con l'esito della decisione.
     *
     * @param richiesta da elaborare da parte del Curatore
     * @param dto dell'esito della verifica della richiesta parte del Curatore
     * @return
     */
    private ResponseEntity<?> elaborazioneRichiesta(Richiesta richiesta, CambiaStatoRichiestaCollaborazioneDTO dto){
        if (!dto.getStato()){
            if (dto.getMessaggioAggiuntivo() == null) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("message", "Inserire un messaggio di rifiuto"));
            }
            String messaggio = "La sua richiesta di collaborazione è stata rifiutata per la seguente motivazione: "
                    + dto.getMessaggioAggiuntivo();
            Optional<Utente> utente = utenteService.selezionaUtenteById(richiesta.getIdMittente());
            String emailUtente = utente.get().getEmail();
            this.servizioEmail.inviaMail(emailUtente, messaggio,
                    "Valutazione Richiesta di "+richiesta.getTipologia().toString());
        }
        richiestaService.valutaRichiesta(richiesta, dto.getStato());
        return ResponseEntity.ok().body(Collections.singletonMap("message",
                dto.getStato() ? "Richiesta accettata con successo." : "Richiesta correttamente rifiutata."));
    }

}
