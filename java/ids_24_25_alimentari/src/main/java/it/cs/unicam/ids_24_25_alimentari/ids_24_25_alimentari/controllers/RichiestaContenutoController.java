package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.*;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.eventi.RichiestaEventoFieraDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.eventi.RichiestaEventoVisitaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.infoAzienda.RichiestaInfoProduttoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.infoAzienda.RichiestaInfoTrasformatoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.prodotti.RichiestaPacchettoDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.prodotti.RichiestaProdottoDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.RichiestaContenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto.RichiestaContenutoService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.multipartConverter.ConvertitoreMultipartFileToFile;
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


import static it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.multipartConverter.ConvertitoreMultipartFileToFile.convertMultipartFileArrayToFileArray;

/**
 * Controller che gestisce le richieste di contenuti.
 */
@Controller
@RequestMapping("/api/richieste-contenuto")
public class RichiestaContenutoController {
    private final RichiestaContenutoService richiestaContenutoService;

    public RichiestaContenutoController(RichiestaContenutoService richiestaContenutoService) {
        this.richiestaContenutoService = richiestaContenutoService;
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
            return richiestaContenutoService.visualizzaContenutoByRichiesta(id);
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
    public ResponseEntity<List<RichiestaContenuto>> getAllRichiesteContenuto(
            @RequestParam (required = false) String sortBy,
            @RequestParam (required = false) String order
    ) {
        List<RichiestaContenuto> richiesteContenuto = this.richiestaContenutoService.getAllRichiesteContenuto(sortBy, order);
        return ResponseEntity.ok(richiesteContenuto);
    }

    /**
     * <h2>Crea una nuova richiesta di informazioni per un produttore.</h2>
     * <p>
     * Questo endpoint consente di creare una nuova richiesta di informazioni per un produttore.
     * Accetta un DTO contenente i dettagli della richiesta e converte i file caricati in oggetti
     * di tipo <code>File</code>.
     * </p>
     * <p><strong>Response:</strong></p>
     * <ul>
     *   <li><code>200 OK</code>: La richiesta è stata creata con successo.</li>
     *   <li><code>400 Bad Request</code>: Errore durante la conversione dei file o parametro non valido.</li>
     * </ul>
     *
     * @param dto Il DTO contenente i dettagli della richiesta.
     * @return Una <code>ResponseEntity</code> contenente la richiesta creata o un messaggio di errore.
     */
    @PostMapping(value = "/informazioni/produttore/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> nuovaRichiestaInfoProduttore(
            @ModelAttribute RichiestaInfoProduttoreDTO dto) {

        File[] immaginiFiles;
        try {
            immaginiFiles = convertMultipartFileArrayToFileArray(
                    dto.getImmagini());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error",
                            "Errore durante la conversione delle immagini: " + e.getMessage()));
        }

        File[] certificatiFiles;
        try {
            certificatiFiles = convertMultipartFileArrayToFileArray(
                    dto.getCertificati());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error",
                            "Errore durante la conversione dei certificati: " + e.getMessage()));
        }

        try {
            RichiestaContenuto richiesta = this.richiestaContenutoService.nuovaRichiestaInformazioniProduttore(
                    dto.getDescrizione(),
                    dto.getProduzione(),
                    dto.getMetodologie(),
                    immaginiFiles,
                    certificatiFiles);
            return ResponseEntity.ok(richiesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Parametro non valido: " + e.getMessage()));
        }
    }

    /**
     * <h2>Crea una nuova richiesta di informazioni per un trasformatore.</h2>
     * <p>
     * Questo endpoint consente di creare una nuova richiesta di informazioni per un trasformatore.
     * Accetta un DTO contenente i dettagli della richiesta e converte i file caricati in oggetti
     * di tipo <code>File</code>.
     * </p>
     * <p><strong>Response:</strong></p>
     * <ul>
     *   <li><code>200 OK</code>: La richiesta è stata creata con successo.</li>
     *   <li><code>400 Bad Request</code>: Errore durante la conversione dei file o parametro non valido.</li>
     * </ul>
     *
     * @param dto Il DTO contenente i dettagli della richiesta.
     * @return Una <code>ResponseEntity</code> contenente la richiesta creata o un messaggio di errore.
     */
    @PostMapping(value = "/informazioni/trasformatore/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> nuovaRichiestaInfoTrasformatore(
            @ModelAttribute RichiestaInfoTrasformatoreDTO dto) {

        File[] immaginiFiles;
        try {
            immaginiFiles = convertMultipartFileArrayToFileArray(
                    dto.getImmagini());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error",
                            "Errore durante la conversione delle immagini: " + e.getMessage()));
        }

        File[] certificatiFiles;
        try {
            certificatiFiles = convertMultipartFileArrayToFileArray(
                    dto.getCertificati());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error",
                            "Errore durante la conversione dei certificati: " + e.getMessage()));
        }

        try {
            RichiestaContenuto richiesta = this.richiestaContenutoService.nuovaRichiestaInformazioniTrasformatore(
                    dto.getDescrizione(),
                    dto.getProduzione(),
                    dto.getMetodologie(),
                    immaginiFiles,
                    certificatiFiles,
                    dto.getAziendeCollegate());
            return ResponseEntity.ok(richiesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Parametro non valido: " + e.getMessage()));
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

            RichiestaContenuto richiestaProdotto = this.richiestaContenutoService.nuovaRichiestaProdotto(
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
            RichiestaContenuto richiestaPacchetto = this.richiestaContenutoService.nuovaRichiestaPacchetto(
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

            RichiestaContenuto richiestaEvento = richiestaContenutoService.nuovaRichiestaFiera(
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

            RichiestaContenuto richiestaEvento = richiestaContenutoService.nuovaRichiestaVisita(
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
     * <h2>Crea una nuova richiesta per un evento di tipo visita aziendale.</h2>
     *
     * <p>
     * Questo metodo accetta un DTO contenente i dettagli dell'evento e crea una
     * nuova richiesta utilizzando il servizio RichiestaContenutoService. Se si verifica un errore
     * durante la conversione del file locandina, restituisce un errore interno del server.
     * </p>
     *
     * @param dto Il DTO contenente i dettagli dell'evento visita aziendale.
     *            Deve includere titolo, descrizione, data di inizio e fine, e locandina.
     * @return Una ResponseEntity contenente la richiesta creata o un messaggio di errore.
     *         <ul>
     *         <li><b>200 OK:</b> Se la richiesta è stata creata con successo.</li>
     *         <li><b>500 Internal Server Error:</b> Se si verifica un errore durante la creazione della richiesta.</li>
     *         </ul>
     */
    @PostMapping("/visita-azienda/new")
    public ResponseEntity<?> nuovaRichiestaVisitaAzienda(@ModelAttribute @Valid RichiestaEventoVisitaDTO dto) {
        try {
            File locandina = ConvertitoreMultipartFileToFile.convertiMultipartFileToFile(dto.getLocandina());

            RichiestaContenuto richiestaEvento = richiestaContenutoService.nuovaRichiestaVisitaAzienda(
                    dto.getTitolo(),
                    dto.getDescrizione(),
                    dto.getInizio(),
                    dto.getFine(),
                    locandina);

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

    @PatchMapping(value = "/{id}/valuta")
    public ResponseEntity<?> valutaRichiesta(@RequestBody ValutaRichiestaDTO dto, @PathVariable long id) {
        return richiestaContenutoService.getRichiestaById(id)
                .map(richiesta -> {
                    if (richiesta.getApprovato() != null) {
                        return ResponseEntity.badRequest()
                                .body(Collections.singletonMap("message", "La richiesta è già stata elaborata"));
                    }
                    return richiestaContenutoService.elaborazioneRichiesta(richiesta, dto);
                })
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(Collections.singletonMap("error", "Richiesta non trovata")));
    }
}