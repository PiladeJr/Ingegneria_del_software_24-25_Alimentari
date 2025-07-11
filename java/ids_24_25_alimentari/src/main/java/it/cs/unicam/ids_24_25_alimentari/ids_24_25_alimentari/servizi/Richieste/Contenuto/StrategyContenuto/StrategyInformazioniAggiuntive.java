package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto.StrategyContenuto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info.InfoAzienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.RichiestaContenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaContenutoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.InfoAziendaService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.UtenteService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.EnumComuni.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StrategyInformazioniAggiuntive implements RichiestaContenutoStrategy {

    private final InfoAziendaService infoAziendaService;
    private final UtenteService utenteService;
    private final RichiestaContenutoRepository richiestaContenutoRepository;

    public StrategyInformazioniAggiuntive(InfoAziendaService infoAziendaService, UtenteService utenteService, RichiestaContenutoRepository richiestaContenutoRepository) {
        this.infoAziendaService = infoAziendaService;
        this.utenteService = utenteService;
        this.richiestaContenutoRepository = richiestaContenutoRepository;
    }

    @Override
    public void processaRichiesta(RichiestaContenuto richiesta, Boolean status) {
        InfoAzienda info = infoAziendaService.getInfoById(richiesta.getTargetId());
        if (info != null) {
            if (info.getStatus()== Status.PENDING){
                if (status){
                    info.setStatus(Status.APPROVATO);
                    richiesta.setStatus(Status.APPROVATO);
                } else {
                    info.setStatus(Status.RIFIUTATO);
                    richiesta.setStatus(Status.RIFIUTATO);
                    info.setAzienda(null);
                }
                infoAziendaService.salvaInformazioniAggiuntive(info);
                Optional<Utente> utente = utenteService.getUtenteById(utenteService.getIdUtenteAutenticato());
                if (utente.isPresent() && utente.get().getRuolo() ==  Ruolo.CURATORE) {
                    richiesta.setIdCuratore(utente.get().getId());
                }
                richiestaContenutoRepository.save(richiesta);
            }
            else {
                throw new IllegalArgumentException("Informazioni già valutate con ID: " + richiesta.getTargetId());
            }
        }
        else {
            throw new IllegalArgumentException("Informazioni non trovate con ID: " + richiesta.getTargetId());
        }
    }


    @Override
        public ResponseEntity<?> visualizzaContenutoByRichiesta(RichiestaContenuto richiesta) {
        return infoAziendaService.getInformazioniAggiuntive( richiesta.getTargetId());
    }


    @Override
    public Tipologia getTipologia() {
        return Tipologia.INFO_AZIENDA;
    }
}
