package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.RichiestaInformazioniAggiuntiveAziendaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.RichiestaProdottoDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.RichiestaService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.ConvertitoreMultipartFileToFile;
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

/**
 * Controller che gestisce le richieste di contenuti.
 */
@Controller
@RequestMapping("/api/richieste-contenuto")
public class RichiestaController {

    private final RichiestaService richiestaService;

    public RichiestaController(RichiestaService richiestaService) {
        this.richiestaService = richiestaService;
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
    @PostMapping(value = "/informazioni-aggiuntive", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
}
