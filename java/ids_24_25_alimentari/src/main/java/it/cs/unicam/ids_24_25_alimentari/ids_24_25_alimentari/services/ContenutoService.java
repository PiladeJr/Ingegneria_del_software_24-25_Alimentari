package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders.InformazioniAggiuntiveBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders.RichiestaBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.InformazioniAggiuntive;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.UtenteAziendaEsterna;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class ContenutoService {
    private final RichiestaRepository richiestaRepository;
    private final InformazioniAggiuntiveRepository informazioniAggiuntiveRepository;
    private final UtenteAziendaEsternaRepository utenteAziendaRepository;
    private final UtenteRepository utenteRepository;
    private final AziendaRepository aziendaRepository;
    InformazioniAggiuntiveBuilder builder;
    RichiestaBuilder richiestaBuilder;

    public ContenutoService(RichiestaRepository richiestaRepository, InformazioniAggiuntiveRepository informazioniAggiuntiveRepository, UtenteAziendaEsternaRepository utenteAziendaRepository, UtenteRepository utenteRepository, AziendaRepository aziendaRepository) {
        this.richiestaRepository = richiestaRepository;
        this.informazioniAggiuntiveRepository = informazioniAggiuntiveRepository;
        this.utenteAziendaRepository = utenteAziendaRepository;
        this.utenteRepository = utenteRepository;
        this.aziendaRepository = aziendaRepository;
    }

    /**
     * metodo per salvare la richiesta
     * @param richesta
     * @return
     */
    public Richiesta salvaRichiesta(Richiesta richesta){
        return richiestaRepository.save(richesta);
    }
//    public Richiesta nuovaRichiestaInformazioni(Tipologia tipo, Long idMittente, String descrizione, String produzione, String metodologie, List<File>immagini, List<File>certificati){
//        RichiestaBuilder richiesta=new RichiestaBuilder();
//        richiesta.costruisciTipologia(tipo);
//        richiesta.costruisciIdMittente(idMittente);
//        if(tipo.equals("InfoAzienda")){
//            //TODO cambiare a modo di switch per i casi Prodotto e Evento
//            long id =nuovaInformazioneAggiuntiva(descrizione, produzione, metodologie, immagini, certificati).getId();
//            richiesta.costruisciIdInformazioni(id);
//        }
//        return salvaRichiesta(richiesta.build());
//    }
    public InformazioniAggiuntive salvaInformazioniAggiuntive(InformazioniAggiuntive info){
        return informazioniAggiuntiveRepository.save(info);
    }
    public InformazioniAggiuntive nuovaInformazioneAggiuntiva(String descrizione,
                                                              String produzione,
                                                              String metodologie,
                                                              List<File> immagini,
                                                              List<File> certificati,
                                                              Long idAziendaProduttrice){
        InformazioniAggiuntiveBuilder builder=new InformazioniAggiuntiveBuilder();
        builder.costruisciDescrizione(descrizione);
        builder.costruisciProduzione(produzione);
        builder.costruisciMetodi(metodologie);
        if (immagini != null) {
            for (File immagine : immagini) {
                builder.aggiungiImmagine(immagine);
            }
        }
        if (certificati != null) {
            for (File certificato : certificati) {
                builder.aggiungiCertificato(certificato);
            }
        }
        Long idUtente= getIdUtenteAutenticato();
        Optional<Utente> utente = utenteRepository.findById(idUtente);
        if(utente.get().getRuolo() == Ruolo.TRASFORMATORE){
        CollegaAzienda(idUtente,idAziendaProduttrice);
        }
        return salvaInformazioniAggiuntive(builder.getInformazioniAggiuntive());
    }

    /**
     * logica per il collegamento delle aziende Trasformatore alle aziende Produttore
     * @param idUtente l'id dell'utente con l'account trasformatore
     * @param idAziendaProduttrice l'id dell'azienda produttrice
     */
    public void CollegaAzienda(long idUtente,Long idAziendaProduttrice){
        UtenteAziendaEsterna collegamento = new UtenteAziendaEsterna();
        Long utente = utenteRepository.findById(idUtente).getId();
        Long azienda = aziendaRepository.findById(idAziendaProduttrice).get().getId();
        if (utente == null) throw new IllegalArgumentException("utente non trovato");
        collegamento.setIdUtente(utente);
        if (azienda == null) throw new IllegalArgumentException("azienda non trovata");
        collegamento.setIdAziendaProduttrice(azienda);
        utenteAziendaRepository.save(collegamento);
    }
    private Long getIdUtenteAutenticato() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<Utente> utente = utenteRepository.findByEmail(userDetails.getUsername());
            return utente != null ? utente.get().getId() : null;
        }
        throw new RuntimeException("Utente non autenticato");
    }
}
