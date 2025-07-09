package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.ValutaRichiestaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.Richieste.RichiestaContenutoBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.Contenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.RichiestaContenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaContenutoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.UtenteRepository;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.EventoService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.InfoAggiuntiveService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.ProdottoService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto.StrategyContenuto.RichiestaContenutoStrategy;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto.StrategyContenuto.RichiestaStrategyFactory;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.RichiestaInterface;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.RichiestaService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.UtenteService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp.ImplementazioneServizioMail;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final InfoAggiuntiveService infoAggiuntiveService;
    private final ProdottoService prodottoService;
    private final EventoService eventoService;
    private RichiestaStrategyFactory strategyFactory;

    public RichiestaContenutoService(RichiestaContenutoRepository richiestaContenutoRepository,
            InfoAggiuntiveService infoAggiuntiveService, ProdottoService prodottoService, EventoService eventoService,
            UtenteService utenteService, ImplementazioneServizioMail mailService,
            RichiestaStrategyFactory strategyFactory) {
        super(mailService, utenteService);
        this.richiestaContenutoRepository = richiestaContenutoRepository;
        this.infoAggiuntiveService = infoAggiuntiveService;
        this.prodottoService = prodottoService;
        this.eventoService = eventoService;

    }

    /**
     * Salva una richiesta nel database.
     *
     * @param richiesta La richiesta da salvare.
     * @return La richiesta creata e salvata nel database.
     */
    public RichiestaContenuto salvaRichiesta(RichiestaContenuto richiesta) {
        return richiestaContenutoRepository.save(richiesta);
    }

    /**
     * Recupera tutte le richieste presenti nel database.
     *
     * @return Una lista di tutte le richieste.
     */
    public List<RichiestaContenuto> getAllRichiesteContenuto(String sortBy) {
        List<RichiestaContenuto> richieste = new ArrayList<>(richiestaContenutoRepository.getAllRichiesteContenuto());
        Comparator<RichiestaContenuto> comparator = switch (sortBy.toLowerCase()) {
            case "tipologia" -> Comparator.comparing(RichiestaContenuto::getTipologia);
            case "stato" -> Comparator.comparing(RichiestaContenuto::getApprovato);
            default -> Comparator.comparingLong(RichiestaContenuto::getId);
        };
        return richieste.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * Recupera una richiesta specifica in base al suo ID.
     *
     * @param id L'ID della richiesta da recuperare.
     * @return Un {@link Optional} contenente la richiesta se trovata, altrimenti
     *         vuoto.
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
        processaRichiesta(richiesta, dto.getStato());
        String emailUtente = (utenteService.getUtenteById(richiesta.getIdMittente())).get().getEmail();
        if (!dto.getStato()) {
            if (dto.getMessaggioAggiuntivo() == null) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("message", "Inserire un messaggio di rifiuto"));
            }
            notificaRifiutoRichiesta(dto.getMessaggioAggiuntivo(), emailUtente, richiesta.getTipoContenuto());
        } else {
            String messaggio = "\"La sua richiesta di inserimento di " + richiesta.getTipoContenuto() + " con ID "
                    + richiesta.getTargetId() +
                    "                + \" è stata accettata con successo!";
            notificaAccettazioneRichiesta(messaggio, emailUtente, richiesta.getTipoContenuto());
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
    public Contenuto visualizzaContenutoByRichiesta(long idRichiesta) {
        RichiestaContenuto richiesta = richiestaContenutoRepository.findById(idRichiesta)
                .orElseThrow(() -> new RuntimeException("Richiesta non trovata"));

        RichiestaContenutoStrategy strategy = strategyFactory.getStrategy(richiesta.getTipologia());
        if (strategy != null) {
            return strategy.visualizzaContenutoByRichiesta(richiesta);
        } else {
            throw new IllegalArgumentException("Tipologia non supportata: " + richiesta.getTipologia());
        }
    }

    public RichiestaContenuto nuovaRichiestaInformazioniTrasformatore(
            String descrizione,
            String produzione,
            String metodologie,
            File[] immagini,
            File[] certificati,
            List<Azienda> aziendeCollegate) {
        long id = this.infoAggiuntiveService
                .nuoveInformazioniTrasformatore(descrizione, produzione, metodologie, immagini, certificati, aziendeCollegate)
                .getId();
        RichiestaContenuto richiesta = this.nuovaRichiesta(id, Tipologia.INFO_AZIENDA, "info_azienda");
        this.notificaNuovaRichiesta(Ruolo.CURATORE);
        return salvaRichiesta(richiesta);
    }

    public RichiestaContenuto nuovaRichiestaInformazioniProduttore(
            String descrizione,
            String produzione,
            String metodologie,
            File[] immagini,
            File[] certificati) {
        long id = this.infoAggiuntiveService
                .nuoveInformazioniProduttore(descrizione, produzione, metodologie, immagini, certificati)
                .getId();
        RichiestaContenuto richiesta = this.nuovaRichiesta(id, Tipologia.INFO_AZIENDA, "info_azienda");
        this.notificaNuovaRichiesta(Ruolo.CURATORE);
        return salvaRichiesta(richiesta);
    }

    /**
     * Crea una nuova richiesta per un nuovo prodotto per un'azienda
     *
     * @param nome        Nome del prodotto
     * @param descrizione Descrizione del prodotto
     * @param idAzienda   Identificativo dell'azienda produttrice
     * @param immagini    File contenenti immagini relative al prodotto
     * @param prezzo      Prezzo del prodotto
     * @param quantita    Quantita del prodotto
     * @param allergeni   Allergeni relativi al prodotto
     * @param tecniche    Tecniche adottate per la realizzazione del prodotto
     * @return la richiesta di prodotto creata e salvata nel database
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
        this.notificaNuovaRichiesta(Ruolo.CURATORE);
        return salvaRichiesta(richiesta);
    }

    /**
     * Crea richiesta per un pacchetto di prodotti
     *
     * @param nome        Nome del pacchetto
     * @param descrizione Descrizione del pacchetto
     * @param prezzo      Prezzo del pacchetto
     * @param prodotti    Prodotti contenuti nel pacchetto
     * @return la richiesta di pacchetto creata e salvata nel database
     */
    public RichiestaContenuto nuovaRichiestaPacchetto(
            String nome,
            String descrizione,
            Double prezzo,
            Set<Long> prodotti) {
        long id = this.prodottoService.nuovoPacchetto(nome, descrizione, prezzo, prodotti).getId();
        RichiestaContenuto richiesta = this.nuovaRichiesta(id, Tipologia.PRODOTTO, "Pacchetto");
        this.notificaNuovaRichiesta(Ruolo.CURATORE);
        return salvaRichiesta(richiesta);
    }

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

        // Notifica i curatori della nuova richiesta
        this.notificaNuovaRichiesta(Ruolo.CURATORE);

        // Salva e restituisce la richiesta
        return salvaRichiesta(richiesta);
    }

    public RichiestaContenuto nuovaRichiestaVisita(
            String titolo,
            String descrizione,
            LocalDateTime inizio,
            LocalDateTime fine,
            File locandina,
            Indirizzo indirizzo,
            Azienda aziendaRiferimento) {
        // Crea l'evento utilizzando EventoService
        Long idEvento;
        idEvento = eventoService.creaVisita(titolo, descrizione, inizio, fine, locandina, indirizzo,
                aziendaRiferimento);

        // Crea una nuova richiesta di tipo evento
        RichiestaContenuto richiesta = this.nuovaRichiesta(idEvento, Tipologia.EVENTO, "visita");

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
