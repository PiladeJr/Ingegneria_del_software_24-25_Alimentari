package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.ValutaRichiestaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.Richieste.RichiestaContenutoBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.RichiestaContenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaContenutoRepository;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.EventoService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.InfoAziendaService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.ProdottoService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto.StrategyContenuto.RichiestaContenutoStrategy;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto.StrategyContenuto.RichiestaStrategyFactory;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.RichiestaService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.UtenteService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.EnumComuni.Status;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp.ImplementazioneServizioMail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servizio responsabile della creazione, del salvataggio e della notifica delle
 * nuove richieste ({@link RichiestaContenuto}).
 */
@Service
public class RichiestaContenutoService extends RichiestaService {
    private final RichiestaContenutoRepository richiestaContenutoRepository;
    private final InfoAziendaService infoAziendaService;
    private final ProdottoService prodottoService;
    private final EventoService eventoService;
    private final RichiestaStrategyFactory strategyFactory;

    public RichiestaContenutoService(RichiestaContenutoRepository richiestaContenutoRepository,
                                     InfoAziendaService infoAziendaService, ProdottoService prodottoService, EventoService eventoService,
                                     UtenteService utenteService, ImplementazioneServizioMail mailService,
                                     RichiestaStrategyFactory strategyFactory) {
        super(mailService, utenteService);
        this.richiestaContenutoRepository = richiestaContenutoRepository;
        this.infoAziendaService = infoAziendaService;
        this.prodottoService = prodottoService;
        this.eventoService = eventoService;
        this.strategyFactory = strategyFactory;
    }

    /**
     * <h2>Salva una richiesta nel database</h2>
     *
     * @param richiesta La richiesta da salvare.
     * @return La richiesta creata e salvata nel database.
     */
    public RichiestaContenuto salvaRichiesta(RichiestaContenuto richiesta) {
        return richiestaContenutoRepository.save(richiesta);
    }

    /**
     * <h2>Recupera tutte le richieste presenti nel database</h2>
     * <p>Questo metodo restituisce una lista di tutte le richieste presenti nel database.</p>
     *
     * @param sortBy Il criterio di ordinamento (es. "tipologia", "pending", "approvate", "rifiutate").
     * @param order  L'ordine di ordinamento ("asc" per crescente, "desc" per decrescente).
     * @return Una lista di tutte le richieste ordinate secondo i parametri forniti.
     */
    public List<RichiestaContenuto> getAllRichiesteContenuto(String sortBy, String order) {
        List<RichiestaContenuto> richieste = new ArrayList<>(richiestaContenutoRepository.findAllRichieste());
        Comparator<RichiestaContenuto> comparator = switch (sortBy.toLowerCase()) {
            case "tipologia" -> Comparator.comparing(RichiestaContenuto::getTipologia);
            case "pending" -> Comparator.comparing(richiesta -> richiesta.getStatus() == Status.PENDING);
            case "approvate" -> Comparator.comparing(richiesta -> richiesta.getStatus() == Status.APPROVATO);
            case "rifiutate" -> Comparator.comparing(richiesta -> richiesta.getStatus() == Status.RIFIUTATO);
            default -> Comparator.comparingLong(RichiestaContenuto::getId);
        };

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return richieste.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * <h2>Recupera una richiesta specifica in base al suo ID</h2>
     * <p>Questo metodo consente di recuperare una richiesta specifica dal database utilizzando il suo ID.</p>
     *
     * @param id L'ID della richiesta da recuperare.
     * @return Un {@link Optional} contenente la richiesta se trovata, altrimenti vuoto.
     */
    public Optional<RichiestaContenuto> getRichiestaById(Long id) {
        return this.richiestaContenutoRepository.findById(id);
    }

    /**
     * <h2>Processa una richiesta in base al suo ID</h2>
     * <br>
     * Questo metodo elabora una richiesta utilizzando la strategia associata alla
     * tipologia della richiesta. Se la tipologia non è supportata, viene sollevata
     * un'eccezione.
     *
     * @param richiesta La richiesta da processare.
     * @param stato     Lo stato della richiesta (approvata o rifiutata).
     * @throws IllegalArgumentException se la tipologia della richiesta non è
     *                                  supportata.
     */
    private void processaRichiesta(RichiestaContenuto richiesta, Boolean stato) {
        RichiestaContenutoStrategy strategy = strategyFactory.getStrategy(richiesta.getTipologia());
        if (strategy != null) {
            strategy.processaRichiesta(richiesta, stato);
        } else {
            throw new IllegalArgumentException("Tipologia non supportata: " + richiesta.getTipologia());
        }
    }


    /**
     * <h2>Elabora una richiesta di contenuto</h2>
     * <br>
     * Questo metodo consente di elaborare una richiesta di contenuto, approvandola o rifiutandola
     * in base allo stato fornito. Se la richiesta viene rifiutata, è necessario fornire un messaggio
     * aggiuntivo. In caso di approvazione, viene inviata una notifica di accettazione.
     *
     * @param richiesta La richiesta di contenuto da elaborare.
     * @param dto       Oggetto contenente lo stato della richiesta (approvata o rifiutata) e un
     *                  eventuale messaggio aggiuntivo in caso di rifiuto.
     * @return Una risposta HTTP che indica il risultato dell'elaborazione della richiesta.
     */
    public ResponseEntity<?> elaborazioneRichiesta(RichiestaContenuto richiesta, ValutaRichiestaDTO dto) {
        String emailUtente = (utenteService.getUtenteById(richiesta.getIdMittente())).get().getEmail();
        if (!dto.getStato()) {
            if (dto.getMessaggio() == null) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("message", "Inserire un messaggio di rifiuto"));
            }
            processaRichiesta(richiesta, false);
            notificaRifiuto(dto.getMessaggio(), emailUtente, richiesta.getTipoContenuto());
        } else {
            processaRichiesta(richiesta, true);
            String messaggio = "\"La sua richiesta di inserimento di " + richiesta.getTipoContenuto() + " con ID "
                    + richiesta.getTargetId() +
                    "                + \" è stata accettata con successo!";
            notificaApprovazione(messaggio, emailUtente, richiesta.getTipoContenuto());
        }
        return ResponseEntity.ok().body(Collections.singletonMap("message",
                dto.getStato() ? "Richiesta accettata con successo." : "Richiesta correttamente rifiutata."));
    }

    /**
     * <h2>Ottiene il contenuto della richiesta tramite il suo ID.</h2>
     * <br>
     * Questo metodo recupera il contenuto associato a una richiesta specifica
     * tramite il suo ID. Utilizza una strategia specifica in base alla
     * tipologia della richiesta per ottenere il contenuto.
     *
     * @param idRichiesta L'ID della richiesta da cui ottenere il contenuto.
     * @return Il contenuto della richiesta, o un'eccezione se non trovata o se la
     *         tipologia non è supportata.
     * @throws IllegalArgumentException se la tipologia della richiesta non è
     *                                  supportata.
     */
    public ResponseEntity<?> visualizzaContenutoByRichiesta(long idRichiesta) {
        RichiestaContenuto richiesta = richiestaContenutoRepository.findById(idRichiesta)
                .orElseThrow(() -> new RuntimeException("Richiesta non trovata"));

        RichiestaContenutoStrategy strategy = strategyFactory.getStrategy(richiesta.getTipologia());
        if (strategy != null) {
            return strategy.visualizzaContenutoByRichiesta(richiesta);
        } else {
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body("Tipologia non supportata: " + richiesta.getTipologia());
        }
    }

    /**
     * <h2>Crea una nuova richiesta di informazioni per un trasformatore</h2>
     * <p>
     * Questo metodo consente di creare una nuova richiesta di informazioni
     * relative a un trasformatore, includendo descrizione, produzione,
     * metodologie, immagini, certificati e aziende collegate.
     * </p>
     *
     * @param descrizione      La descrizione delle informazioni del trasformatore.
     * @param produzione       La produzione del trasformatore.
     * @param metodologie      Le metodologie utilizzate dal trasformatore.
     * @param immagini         File contenenti immagini relative al trasformatore.
     * @param certificati      File contenenti certificati relativi al trasformatore.
     * @param aziendeCollegate Lista di aziende collegate al trasformatore.
     * @return La richiesta di contenuto creata e salvata nel database.
     */
    public RichiestaContenuto nuovaRichiestaInformazioniTrasformatore(
            String descrizione,
            String produzione,
            String metodologie,
            File[] immagini,
            File[] certificati,
            List<Azienda> aziendeCollegate) {
        long id = this.infoAziendaService
                .nuoveInformazioniTrasformatore(descrizione, produzione, metodologie, immagini, certificati, aziendeCollegate)
                .getId();
        RichiestaContenuto richiesta = this.nuovaRichiesta(id, Tipologia.INFO_AZIENDA, "info_azienda");
        richiesta.setStatus(Status.PENDING);
        this.notificaNuovaRichiesta(Ruolo.CURATORE);
        return salvaRichiesta(richiesta);
    }

    /**
     * <h2>Crea una nuova richiesta di informazioni per un produttore</h2>
     * <p>
     * Questo metodo consente di creare una nuova richiesta di informazioni
     * relative a un produttore, includendo descrizione, produzione,
     * metodologie, immagini e certificati.
     * </p>
     *
     * @param descrizione La descrizione delle informazioni del produttore.
     * @param produzione La produzione del produttore.
     * @param metodologie Le metodologie utilizzate dal produttore.
     * @param immagini File contenenti immagini relative al produttore.
     * @param certificati File contenenti certificati relativi al produttore.
     * @return La richiesta di contenuto creata e salvata nel database.
     */
    public RichiestaContenuto nuovaRichiestaInformazioniProduttore(
            String descrizione,
            String produzione,
            String metodologie,
            File[] immagini,
            File[] certificati) {
        long id = this.infoAziendaService
                .nuoveInformazioniProduttore(descrizione, produzione, metodologie, immagini, certificati)
                .getId();
        RichiestaContenuto richiesta = this.nuovaRichiesta(id, Tipologia.INFO_AZIENDA, "info_azienda");
        richiesta.setStatus(Status.PENDING);
        this.notificaNuovaRichiesta(Ruolo.CURATORE);
        return salvaRichiesta(richiesta);
    }

    /**
     * <h2>Crea una nuova richiesta per un nuovo prodotto per un'azienda</h2>
     * <p>
     * Questo metodo consente di creare una nuova richiesta per un prodotto
     * associato a un'azienda specifica. Include informazioni come nome, descrizione,
     * immagini, prezzo, quantità, allergeni e tecniche di realizzazione.
     * </p>
     *
     * @param nome        Nome del prodotto.
     * @param descrizione Descrizione del prodotto.
     * @param idAzienda   Identificativo dell'azienda produttrice.
     * @param immagini    File contenenti immagini relative al prodotto.
     * @param prezzo      Prezzo del prodotto.
     * @param quantita    Quantità del prodotto.
     * @param allergeni   Allergeni relativi al prodotto.
     * @param tecniche    Tecniche adottate per la realizzazione del prodotto.
     * @return La richiesta di prodotto creata e salvata nel database.
     */
    public RichiestaContenuto nuovaRichiestaProdotto(
            String nome,
            String descrizione,
            Long idAzienda,
            File[] immagini,
            double prezzo,
            int quantita,
            String allergeni,
            String tecniche) {
        long id = this.prodottoService
                .nuovoProdotto(nome, descrizione, idAzienda, immagini, prezzo, quantita, allergeni, tecniche).getId();
        RichiestaContenuto richiesta = this.nuovaRichiesta(id, Tipologia.PRODOTTO, "singolo");
        richiesta.setStatus(Status.PENDING);
        this.notificaNuovaRichiesta(Ruolo.CURATORE);
        return salvaRichiesta(richiesta);
    }

    /**
     * <h2>Crea richiesta per un pacchetto di prodotti</h2>
     * <p>
     * Questo metodo consente di creare una nuova richiesta per un pacchetto di prodotti.
     * Include informazioni come nome, descrizione, prezzo e i prodotti contenuti nel pacchetto.
     * </p>
     *
     * @param nome        Nome del pacchetto.
     * @param descrizione Descrizione del pacchetto.
     * @param prezzo      Prezzo del pacchetto.
     * @param prodotti    Prodotti contenuti nel pacchetto.
     * @return La richiesta di pacchetto creata e salvata nel database.
     */
    public RichiestaContenuto nuovaRichiestaPacchetto(
            String nome,
            String descrizione,
            Double prezzo,
            Set<Long> prodotti) {
        long id = this.prodottoService.nuovoPacchetto(nome, descrizione, prezzo, prodotti).getId();
        RichiestaContenuto richiesta = this.nuovaRichiesta(id, Tipologia.PRODOTTO, "Pacchetto");
        richiesta.setStatus(Status.PENDING);
        this.notificaNuovaRichiesta(Ruolo.CURATORE);
        return salvaRichiesta(richiesta);
    }

    /**
     * <h2>Crea una nuova richiesta di tipo fiera</h2>
     * <p>
     * Questo metodo consente di creare una nuova richiesta per un evento di tipo fiera.
     * Include informazioni come titolo, descrizione, data di inizio e fine, locandina, indirizzo e aziende partecipanti.
     * </p>
     *
     * @param titolo        Il titolo della fiera.
     * @param descrizione   La descrizione della fiera.
     * @param inizio        La data e ora di inizio della fiera.
     * @param fine          La data e ora di fine della fiera.
     * @param locandina     File contenente la locandina della fiera.
     * @param indirizzo     L'indirizzo in cui si svolgerà la fiera.
     * @param aziende       La lista delle aziende partecipanti alla fiera.
     * @return La richiesta di contenuto creata e salvata nel database.
     */
    public RichiestaContenuto nuovaRichiestaFiera(
            String titolo,
            String descrizione,
            LocalDateTime inizio,
            LocalDateTime fine,
            File locandina,
            Indirizzo indirizzo,
            List<Azienda> aziende) {
        // Crea l'evento utilizzando EventoService
        Long idEvento;
        idEvento = eventoService.creaFiera(titolo, descrizione, inizio, fine, locandina, indirizzo, aziende);

        // Crea una nuova richiesta di tipo evento
        RichiestaContenuto richiesta = this.nuovaRichiesta(idEvento, Tipologia.EVENTO, "fiera");

        richiesta.setStatus(Status.PENDING);

        // Notifica i curatori della nuova richiesta
        this.notificaNuovaRichiesta(Ruolo.CURATORE);

        // Salva e restituisce la richiesta
        return salvaRichiesta(richiesta);
    }

    /**
     * <h2>Crea una nuova richiesta di tipo visita</h2>
     * <p>
     * Questo metodo consente di creare una nuova richiesta per un evento di tipo visita.
     * Include informazioni come titolo, descrizione, data di inizio e fine, locandina, indirizzo e azienda di riferimento.
     * </p>
     *
     * @param titolo            Il titolo della visita.
     * @param descrizione       La descrizione della visita.
     * @param inizio            La data e ora di inizio della visita.
     * @param fine              La data e ora di fine della visita.
     * @param locandina         File contenente la locandina della visita.
     * @param indirizzo         L'indirizzo in cui si svolgerà la visita.
     * @param aziendaRiferimento L'azienda di riferimento per la visita.
     * @return La richiesta di contenuto creata e salvata nel database.
     */
    public RichiestaContenuto nuovaRichiestaVisita(
            String titolo,
            String descrizione,
            LocalDateTime inizio,
            LocalDateTime fine,
            File locandina,
            Indirizzo indirizzo,
            Azienda aziendaRiferimento) {
        // Crea l'evento utilizzando EventoService
        Long idEvento = eventoService.creaVisita(titolo, descrizione, inizio, fine, locandina, indirizzo,
                aziendaRiferimento);

        // Crea una nuova richiesta di tipo evento
        RichiestaContenuto richiesta = this.nuovaRichiesta(idEvento, Tipologia.EVENTO, "visita");

        richiesta.setStatus(Status.PENDING);

        // Notifica i curatori della nuova richiesta
        this.notificaNuovaRichiesta(Ruolo.CURATORE);

        // Salva e restituisce la richiesta
        return salvaRichiesta(richiesta);
    }

    /**
     * <h2>Crea una nuova richiesta di tipo visita aziendale</h2>
     * <p>
     * Questo metodo consente di creare una nuova richiesta per un evento di tipo visita aziendale.
     * Include informazioni come titolo, descrizione, data di inizio e fine, e locandina.
     * </p>
     *
     * @param titolo      Il titolo della visita aziendale.
     * @param descrizione La descrizione della visita aziendale.
     * @param inizio      La data e ora di inizio della visita aziendale.
     * @param fine        La data e ora di fine della visita aziendale.
     * @param locandina   File contenente la locandina della visita aziendale.
     * @return La richiesta di contenuto creata e salvata nel database.
     */
    public RichiestaContenuto nuovaRichiestaVisitaAzienda(
            String titolo,
            String descrizione,
            LocalDateTime inizio,
            LocalDateTime fine,
            File locandina) {
        // Crea l'evento utilizzando il metodo `creaVisitaAzienda` di EventoService
        Long idEvento = eventoService.creaVisitaAzienda(titolo, descrizione, inizio, fine, locandina);

        // Crea una nuova richiesta di tipo evento
        RichiestaContenuto richiesta = this.nuovaRichiesta(idEvento, Tipologia.EVENTO, "visita_azienda");

        richiesta.setStatus(Status.PENDING);

        // Notifica i curatori della nuova richiesta
        this.notificaNuovaRichiesta(Ruolo.CURATORE);

        // Salva e restituisce la richiesta
        return salvaRichiesta(richiesta);
    }

    private RichiestaContenuto nuovaRichiesta(Long idContenuto, Tipologia tipologia, String tipoContenuto) {
        RichiestaContenutoBuilder richiesta = new RichiestaContenutoBuilder();
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

}
