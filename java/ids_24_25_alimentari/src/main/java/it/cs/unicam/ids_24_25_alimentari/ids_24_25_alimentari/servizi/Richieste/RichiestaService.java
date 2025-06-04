package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.eventi.RichiestaEventoVisitaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.RichiestaBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.Contenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.UtenteRepository;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.EventoService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.InfoAggiuntiveService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.ProdottoService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Strategy.RichiestaStrategy;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Strategy.RichiestaStrategyFactory;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.UtenteService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp.ImplementazioneServizioMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Servizio responsabile della creazione, del salvataggio e della notifica delle nuove richieste ({@link Richiesta}).
 */
@Service
public class RichiestaService {
    @Autowired
    private final RichiestaRepository richiestaRepository;
    @Autowired
    private final UtenteRepository utenteRepository;
    @Autowired
    private final InfoAggiuntiveService infoAggiuntiveService;
    @Autowired
    private final ProdottoService prodottoService;
    @Autowired
    private final EventoService eventoService;
    @Autowired
    private final UtenteService utenteService;
    @Autowired
    private RichiestaStrategyFactory strategyFactory;

    public RichiestaService(RichiestaRepository richiestaRepository, UtenteRepository utenteRepository, InfoAggiuntiveService infoAggiuntiveService, ProdottoService prodottoService, EventoService eventoService, UtenteService utenteService) {
        this.richiestaRepository = richiestaRepository;
        this.utenteRepository = utenteRepository;
        this.infoAggiuntiveService = infoAggiuntiveService;
        this.prodottoService = prodottoService;
        this.eventoService = eventoService;
        this.utenteService = utenteService;
    }

    /**
     * Salva una richiesta nel database.
     *
     * @param richiesta La richiesta da salvare.
     * @return La richiesta creata e salvata nel database.
     */
    public Richiesta salvaRichiesta(Richiesta richiesta) {
        return richiestaRepository.save(richiesta);
    }

    public List<Richiesta> getAllRichiesteContenuto() {
        return this.richiestaRepository.getAllRichiesteContenuto();
    }

    public List<Richiesta> getRichiesteByTipo(Tipologia tipologia) { return this.richiestaRepository.getRichiesteByTipo(tipologia); }

    public Optional<Richiesta> getRichiestaById(Long id) {
        return this.richiestaRepository.findById(id);
    }


    /**
     * <h2>Processa una richiesta in base al suo ID</h2>
     * <br>
     * Questo metodo elabora una richiesta identificata dal suo ID utilizzando la strategia
     * associata alla tipologia della richiesta. Se la tipologia non è supportata, viene
     * sollevata un'eccezione.
     *
     * @param idRichiesta L'ID della richiesta da processare.
     * @throws RuntimeException se la richiesta con l'ID specificato non viene trovata.
     * @throws IllegalArgumentException se la tipologia della richiesta non è supportata.
     */
    public void processaRichiesta(long idRichiesta) {
        Richiesta richiesta = richiestaRepository.findById(idRichiesta)
                .orElseThrow(() -> new RuntimeException("Richiesta non trovata"));

        RichiestaStrategy strategy = strategyFactory.getStrategy(richiesta.getTipologia());
        if (strategy != null) {
            strategy.processaRichiesta(richiesta);
        } else {
            throw new IllegalArgumentException("Tipologia non supportata: " + richiesta.getTipologia());
        }
    }

    /**
     * Ottiene una richiesta in base al suo ID e alla sua tipologia.
     *
     * Visualizza la richiesta e le sue componenti ottenute tramite
     * la strategia associata alla tipologia della richiesta.
     *
     * @param idRichiesta L'ID della richiesta da ottenere.
     * @return La richiesta ottenuta, o un'eccezione se non trovata o se la tipologia non è supportata.
     */
    public Contenuto ottieniRichiesta(long idRichiesta){
        Richiesta richiesta = richiestaRepository.findById(idRichiesta)
                .orElseThrow(() -> new RuntimeException("Richiesta non trovata"));

        RichiestaStrategy strategy = strategyFactory.getStrategy(richiesta.getTipologia());
        if (strategy != null) {
            return strategy.ottieniRichiesta(richiesta);
        } else {
            throw new IllegalArgumentException("Tipologia non supportata: " + richiesta.getTipologia());
        }
    }


    /**
     * Crea una nuova richiesta di informazioni aggiuntive per un'azienda.
     *
     * @param descrizione  Descrizione aggiuntiva dell'azienda.
     * @param produzione   Informazioni sulla produzione dell'azienda.
     * @param metodologie  Metodologie di produzione utilizzate dall'azienda.
     * @param immagini     File contenenti immagini relative alle informazioni aggiuntive.
     * @param certificati  File contenenti eventuali certificazioni dell'azienda.
     * @param idAzienda    Identificativi dell'azienda coinvolta.
     * @return La richiesta di informazioni aggiuntive creata e salvata nel database.
     */
    public Richiesta nuovaRichiestaInformazioniAggiuntive(
            String descrizione,
            String produzione,
            String metodologie,
            File[] immagini,
            File[] certificati,
            Long[] idAzienda
    ) {
        long id = this.infoAggiuntiveService.nuovaInformazioneAggiuntiva(descrizione, produzione, metodologie, immagini, certificati, idAzienda).getId();
        Richiesta richiesta = this.nuovaRichiesta(id, Tipologia.INFO_AZIENDA, "info_azienda");
        this.notificaNuovaRichiesta();
        return salvaRichiesta(richiesta);
    }


    /**
     * Crea una nuova richiesta per un nuovo prodotto per un'azienda
     *
     * @param nome          Nome del prodotto
     * @param descrizione   Descrizione del prodotto
     * @param idAzienda     Identificativo dell'azienda produttrice
     * @param immagini      File contenenti immagini relative al prodotto
     * @param prezzo        Prezzo del prodotto
     * @param quantita      Quantita del prodotto
     * @param allergeni     Allergeni relativi al prodotto
     * @param tecniche      Tecniche adottate per la realizzazione del prodotto
     * @return la richiesta di prodotto creata e salvata nel database
     */
    public Richiesta nuovaRichiestaProdotto(
            String nome,
            String descrizione,
            Long idAzienda,
            File[] immagini,
            double prezzo,
            int quantita,
            String allergeni,
            String tecniche
    ) {
        long id = this.prodottoService.nuovoProdotto(nome, descrizione, idAzienda, immagini, prezzo, quantita, allergeni, tecniche).getId();
        Richiesta richiesta = this.nuovaRichiesta(id, Tipologia.PRODOTTO, "singolo");
        this.notificaNuovaRichiesta();
        return salvaRichiesta(richiesta);
    }


    /**
     * Crea richiesta per un pacchetto di prodotti
     *
     * @param nome          Nome del pacchetto
     * @param descrizione   Descrizione del pacchetto
     * @param prezzo        Prezzo del pacchetto
     * @param prodotti      Prodotti contenuti nel pacchetto
     * @return la richiesta di pacchetto creata e salvata nel database
     */
    public Richiesta nuovaRichiestaPacchetto(
            String nome,
            String descrizione,
            Double prezzo,
            Set<Long> prodotti
    ) {
       long id = this.prodottoService.nuovoPacchetto(nome, descrizione, prezzo, prodotti).getId();
        Richiesta richiesta = this.nuovaRichiesta(id, Tipologia.PRODOTTO, "Pacchetto");
        this.notificaNuovaRichiesta();
        return salvaRichiesta(richiesta);
    }



    public Richiesta nuovaRichiestaFiera(
            String titolo,
            String descrizione,
            LocalDateTime inizio,
            LocalDateTime fine,
            File locandina,
            Indirizzo indirizzo,
            List<Azienda> aziende
    ) {
        // Crea l'evento utilizzando EventoService
        Long idEvento;
        idEvento = eventoService.creaFiera(titolo, descrizione, inizio, fine, locandina, indirizzo, aziende);

        // Crea una nuova richiesta di tipo evento
        Richiesta richiesta = this.nuovaRichiesta(idEvento, Tipologia.EVENTO, "fiera");

        // Notifica i curatori della nuova richiesta
        this.notificaNuovaRichiesta();

        // Salva e restituisce la richiesta
        return salvaRichiesta(richiesta);
    }


    public Richiesta nuovaRichiestaVisita(
            String titolo,
            String descrizione,
            LocalDateTime inizio,
            LocalDateTime fine,
            File locandina,
            Indirizzo indirizzo,
            Azienda aziendaRiferimento
    ) {
        // Crea l'evento utilizzando EventoService
        Long idEvento;
        idEvento = eventoService.creaVisita(titolo, descrizione, inizio, fine, locandina, indirizzo, aziendaRiferimento);

        // Crea una nuova richiesta di tipo evento
        Richiesta richiesta = this.nuovaRichiesta(idEvento, Tipologia.EVENTO, "visita");

        // Notifica i curatori della nuova richiesta
        this.notificaNuovaRichiesta();

        // Salva e restituisce la richiesta
        return salvaRichiesta(richiesta);
    }


    //TODO VALUTARE SE SPOSTARE IN UN DIRECTOR O UN FACTORY
    private Richiesta nuovaRichiesta(Long idContenuto, Tipologia tipologia, String tipoContenuto) {
        RichiestaBuilder richiesta = new RichiestaBuilder();
        richiesta.costruisciTipologia(tipologia);
        richiesta.costruisciTargetId(idContenuto);
        richiesta.costruisciTipoContenuto(tipoContenuto);

        Long idMittente = this.utenteService.getIdUtenteAutenticato();
        if (idMittente == null)
            throw new IllegalArgumentException("Utente non trovato");
        else
            richiesta.costruisciIdMittente(idMittente);

        return richiesta.build();
    }

    /**
     * Notifica i curatori dell'inserimento di una nuova richiesta nel database.
     * Viene inviata un'email a tutti gli utenti con ruolo Curatore.
     */
    public void notificaNuovaRichiesta() {
        List<Utente> curatori = this.utenteRepository.findAllByRuolo(Ruolo.CURATORE);
        ImplementazioneServizioMail mailService = new ImplementazioneServizioMail();
        String messaggio = "Una nuova richiesta in attesa di valutazione è stata salvata nel database.";
        String oggetto = "Notifica Nuova Richiesta";

        for (Utente curatore : curatori) {
            mailService.inviaMail(curatore.getEmail(), messaggio, oggetto);
        }
    }

    
//    public Richiesta valutaRichiesta(Richiesta richiesta, Boolean stato) {
//        richiesta.setApprovato(stato);
//        return this.salvaRichiesta(richiesta);
//    }


}
