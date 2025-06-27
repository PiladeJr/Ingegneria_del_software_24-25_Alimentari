package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.*;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.eventi.RichiestaEventoFieraDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.eventi.RichiestaEventoVisitaDTO;import t.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.RichiestaContenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto.import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.UtenteService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp.ServizioEmail;import t.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.multipartConverter.ConvertitoreMultipartFileToFile;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RichiestaContenutoController {

    @Autowired
    private final RichiestaContenutoService richiestaContenutoService;
    @Autowired
    private final ServizioEmail servizioEmail;
    @Autowired
    private final UtenteService utenteService;

    public RichiestaContenutoController(RichiestaContenutoService richiestaContenutoService, ServizioEmail servizioEmail,
                                        UtenteService utenteService) {
        this.richiestaContenutoService = richiestaContenutoService;
        this.servizioEmail = servizioEmail;
        this.utenteService = utenteService;
    }

    /**
     * Restituisce una richiesta di contenuto specifica in base al suo ID.
     *
     

         */
         GetMapping("/visualizza/{id}")
         u
            try {
         
           
                return richiesta.isPres
                        : ResponseEntity.status(404)
                     

                        
                        tch (Exception e) {
                                        esponseEntity.status(500).bo
                                                        
                                                        

                
                        tuisce tutte le richieste di contenuto.
                
         

        @Ge
         ublic ResponseEntity<List<RichiestaContenuto>>
          
            return ResponseEntity.ok(richiesteContenuto
         
        
        /**
                        
                rea una nuova richiesta di informazioni aggiuntive per un'azienda.
                                
                
         

         */
         PostMapping(value = "/informazioni-aggiuntive/new", consumes = MediaT
         u
                @ModelAttribute @Valid RichiestaInformazioniAggiuntiveAziendaDTO 
         
           
                        .convertMultipartFileArrayToFileArray(infoAggiuntiveDTO.getImmagini());
                File[] certificati = ConvertitoreMultipartFileToFile
                                .convertMultipartFileArrayToFileArray(infoAggiuntiveDTO.getCertificati());
                        

                RichiestaContenuto richiestaInfoAggiuntive = this
                                infoAggiuntiveDTO.getDescrizione(),
                        infoAggiuntiveDTO.getProduzione(),
                                infoAggiuntiveDTO.getMetodologie(),

                        certificati,
                                
                                                infoAggiuntiveDTO.getAziendeCollega
                                                
                                                esponseEntity.ok(richiestaInfoAggiu
                                                
                                                
                                                

                
         

         */
         PostMapping(value = "/prodotto/singolo/
         u
                @ModelAttribute @Valid RichiestaProdottoDTO prodottoDTO) throws IOExc
         
           
                        .convertMultipartFileArrayToFileArray(prodottoDTO.getImmagini());
        
                        RichiestaContenuto richiestaProdotto = this.richiestaContenutoService.nuova R

                        prodottoDTO.getDescrizione(),
                                prodottoDTO.getIdAzienda(),

                        prodottoDTO.getPrezzo(),
                                prodottoDTO.getQuantit
                                prodottoDTO.getAllergeni(),
                                prodottoDTO.getTecniche());
                                
                                esponseEntity.ok(richies
                                
                                
                                

                esponseEntity<?> nuovaRichiestaPacchetto(
         

            RichiestaContenuto richiestaPacchetto = this.richiestaContenutoService.nuovaRichiestaPacchetto(
                    pacchettoDTO.getNome(),
                    pacchettoDTO.getDescrizione(),
                    pacchettoDTO.getPrezzo(),
                    pacchettoDTO.getProdotti());

            return ResponseEntity.ok(richiestaPacchetto);

    }

    /**
     * <h2>Crea una nuova richiesta per un evento di tipo fiera.</h2>
     *
     * <p>
     * Questo metodo accetta un DTO contenente i dettagli dell'evento e crea una
     * nuova richiesta
     * utilizzando il servizio RichiestaService. Se si verifica un errore durante la
     

         * 
         *
         *
         *    
         *            locandina,
         *            indi
         * @return Una ResponseEntity contenente la richiesta creata o un messaggio di
         *         err
         *         <ul>
         *     
         *
         *         durante la creazione della richiesta.</li>
         *         </ul>
         */
         PostMapping("/fiera/new")
         ublic ResponseEntity<?> nuovaRichiestaFiera(@ModelAttribute @Valid RichiestaE
         
                File lo
         
                RichiestaContenuto richiestaEvento = richiestaContenutoService.nu
                        dto.getTitolo(),
                        
           
                        dto.getFin
                        locandina,
                         

                        dto.getAziendePresenti());

                return ResponseEntity.ok(richiestaEvento);
                                
                                
                                
                                nuova richiest
                                
                                
                                 accetta un DTO contenente

                zzando il servizio <code>RichiestaService<
         

         * 
         *
         *
         *    
         *            locandina,
         *            indi
         * @return Una <code>ResponseEntity</code> contenente la richiesta creata o
         *         messaggio di errore.
         *         <ul>
         *     
         *
         *         durante la creazione della richiesta.</li>
         *         </ul>
         */
         PostMapping("/visita/new")
         ublic ResponseEntity<?> nuovaRichiestaVisita(@ModelAttribute @Valid Richiesta
         
                File lo
         
                RichiestaContenuto richiestaEvento = richiestaContenutoService.nu
                        dto.getTitolo(),
                        
           
                        dto.getFine
                        locandina,
                         

                        dto.getAziendaRiferimento());

                return ResponseEntity.ok(richiestaEvento);
                                
                                
                                
                                se la richiest
                                >
                                
                                

                priato.
         

         * 
         * @return una ResponseEntity contenente:
         *         - La r
         *
         *    
         */
         PatchMapping(v
         ublic ResponseEntity<?> valutaRichiesta(@RequestBody CambiaStatoR
          
                    .map(richiesta -> {
                        if (richiesta.getApprovat
                            return ResponseEntity.badRequest()
                                    .body(Collections.singletonMap("message", "La richi
                        }
           
                    })
                    .orElseGet(() -> ResponseEntity.status(404)
                                .body(Collections.singletonMap("error", "Richi
                                
                                        
                                                
                                                                ase alla decisione del Curatore, che deci
                                                                                e se
                                        r
                                        mail all'utente che ha effettuato tale richie
                                is
                                
                                                da elaborare da parte del Curatore
         

         */
         rivate ResponseEntity<?> elaborazioneRichiesta(RichiestaContenuto richies
            if (!dto.getStato()) {
                if (dto.getMessaggioAggiuntivo() == null) {
                    return 
          
                }
                String messaggio = "La sua richiesta di collaborazione è stata rifiutata
                  
           
                String emailUtente = utente.getEmail();
                        
                    this.servizioEmail
                                "Valutazione Richiesta di " + richi
                                
                                                toService.processaRichiesta(richiesta, dt
                                                                .getStato());
                        r
                            dto.getStato() ? "Richiesta accettata con successo." : "Richiesta correttamente rifiutata."));
                                        
                        
                        
                        
                                        
                
                
                
                                
                                                
        