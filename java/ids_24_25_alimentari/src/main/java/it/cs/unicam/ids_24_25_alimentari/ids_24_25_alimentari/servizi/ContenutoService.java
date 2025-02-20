package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;


import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.InformazioniAggiuntiveBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.RichiestaBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.InformazioniAggiuntive;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.UtenteAziendaEsterna;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
public class ContenutoService {
    private final RichiestaRepository richiestaRepository;
    private final InformazioniAggiuntiveRepository informazioniAggiuntiveRepository;
    private final UtenteRepository utenteRepository;
    private final AziendaRepository aziendaRepository;
    private final UtenteAziendaEsternaRepository utenteAziendaEsternaRepository;
   //private final ServizioEmail servizioEmail;
    InformazioniAggiuntiveBuilder builder;
    RichiestaBuilder richiestaBuilder;



    public ContenutoService(RichiestaRepository richiestaRepository, InformazioniAggiuntiveRepository informazioniAggiuntiveRepository, UtenteRepository utenteRepository, AziendaRepository aziendaRepository, UtenteAziendaEsternaRepository utenteAziendaEsternaRepository) {

        this.richiestaRepository = richiestaRepository;
        this.informazioniAggiuntiveRepository = informazioniAggiuntiveRepository;
        this.utenteRepository = utenteRepository;
        this.aziendaRepository = aziendaRepository;
        this.utenteAziendaEsternaRepository = utenteAziendaEsternaRepository;
    }

    /**
     * metodo per salvare la richiesta
     * 
     * @param richesta
     * @return
     */
    public Richiesta salvaRichiesta(Richiesta richesta) {
        return richiestaRepository.save(richesta);
    }

    /**
     * crea una nuova richiesta di inserimento nella piattaforma
     * di informazioni aggiuntive per l'azienda
     * @param tipo il tipo di richiesta inviato
     * @param descrizione la descrizione dell'azienda nel dettaglio
     * @param produzione il tipo di prodotto di cui l'azienda si occupa
     * @param metodologie le tecniche di raccolta o di trasformazione utilizzate dall'azienda
     * @param immagini immagini inviate dall'azienda
     * @param certificati i certificati di qualità dell'azienda
     * @param idAzienda la lista di aziende coinvolte nel processo di trasformazione
     * @return la richiesta salvata nella repository
     */
    public Richiesta nuovaRichiestaInformazioni(Tipologia tipo, String descrizione, String produzione,
                                                String metodologie, File[] immagini, File[] certificati, Long[] idAzienda) {
        RichiestaBuilder richiesta = new RichiestaBuilder();
        richiesta.costruisciTipologia(tipo);

        if (tipo.equals(Tipologia.InfoAzienda)) {
            // TODO cambiare a modo di switch per i casi Prodotto e Evento
            long id = nuovaInformazioneAggiuntiva(descrizione, produzione, metodologie, immagini, certificati,idAzienda).getId();
            richiesta.costruisciIdInformazioni(id);
        }

        Long idMittente = getIdUtenteAutenticato();
        if(idMittente==null)
            throw new IllegalArgumentException("utente non trovato");
        else
            richiesta.costruisciIdMittente(idMittente);
        //TODO l'invio della richiesta a tutti gli account di tipo curatore
        return salvaRichiesta(richiesta.build());
    }

    /**
     * salva le informazioni dell'azienda nella repository
     * @param info l'oggetto InformazionAggiuntive salvato in repository
     * @return l'oggetto salvato in repository
     */
    public InformazioniAggiuntive salvaInformazioniAggiuntive(InformazioniAggiuntive info) {
        return informazioniAggiuntiveRepository.save(info);
    }

    public InformazioniAggiuntive nuovaInformazioneAggiuntiva(String descrizione,
            String produzione,
            String metodologie,
            File[] immagini,
            File[] certificati,
            Long[] idAzienda) {
        InformazioniAggiuntiveBuilder builder = new InformazioniAggiuntiveBuilder();
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

        Long idUtente = isTrasformatore();
        if (idUtente!= null)
        {
           for (long id:idAzienda){
               UtenteAziendaEsterna collegamento = CollegaAzienda(idUtente, id);
               if(collegamento== null)
                    throw new IllegalArgumentException("errore durante il collegamento delle aziende");
           }

        }

        return salvaInformazioniAggiuntive(builder.getInformazioniAggiuntive());
    }

    /**
     * logica per il collegamento delle aziende Trasformatore alle aziende
     * Produttore
     * 
     * @param idUtente             l'id dell'utente con l'account trasformatore
     * @param idAziendaProduttrice l'id dell'azienda produttrice
     */

    public UtenteAziendaEsterna CollegaAzienda(Long idUtente, Long idAziendaProduttrice){
        UtenteAziendaEsterna collegamento = new UtenteAziendaEsterna();
        Azienda azienda = aziendaRepository.findAziendaByIdAndruolo(idAziendaProduttrice,Ruolo.PRODUTTORE);
        if (azienda == null) throw new IllegalArgumentException("azienda non trovata");
        collegamento.setUtenteId(idUtente);
        collegamento.setAziendaId(azienda.getId());

        return utenteAziendaEsternaRepository.save(collegamento);
    }

    /**
     * controlla che l'account abbia i permessi
     * di tipo TRASFORMATORE
     *
     * @return l'id dell'account trasformatore se trovato nel sistema
     * null se non ha trovato l'account oppure non ha il ruolo TRASFORMATORE
     */
    public Long isTrasformatore(){
        Long idUtente= getIdUtenteAutenticato();
        Optional<Utente> utente = utenteRepository.findById(idUtente);
        if (utente.get().getRuolo() == Ruolo.TRASFORMATORE)
            return idUtente;
        else
            return null;
    }

    /**
     * ottieni l'id dell'utente autenticato estraendolo dal suo token
     * @return l'id dell'utente autenticato
     */

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
