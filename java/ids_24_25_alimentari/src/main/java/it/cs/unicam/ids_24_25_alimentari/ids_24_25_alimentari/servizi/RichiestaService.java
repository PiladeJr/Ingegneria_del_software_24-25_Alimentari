package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.RichiestaBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.UtenteRepository;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp.ImplementazioneServizioMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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
    private final UtenteService utenteService;

    public RichiestaService(RichiestaRepository richiestaRepository, UtenteRepository utenteRepository, InfoAggiuntiveService infoAggiuntiveService, ProdottoService prodottoService, UtenteService utenteService) {
        this.richiestaRepository = richiestaRepository;
        this.utenteRepository = utenteRepository;
        this.infoAggiuntiveService = infoAggiuntiveService;
        this.prodottoService = prodottoService;
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
        Richiesta richiesta = this.nuovaRichiesta(id, Tipologia.InfoAzienda);
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
        Richiesta richiesta = this.nuovaRichiesta(id, Tipologia.Prodotto);
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
        Richiesta richiesta = this.nuovaRichiesta(id, Tipologia.Prodotto);
        this.notificaNuovaRichiesta();
        return salvaRichiesta(richiesta);
    }

    /**
     * Crea una nuova richiesta associata a un contenuto specifico.
     *
     * @param idContenuto L'ID del contenuto a cui è associata la richiesta.
     * @return La richiesta creata.
     * @throws IllegalArgumentException se l'utente autenticato non è trovato.
     */
    private Richiesta nuovaRichiesta(Long idContenuto, Tipologia tipologia) {
        RichiestaBuilder richiesta = new RichiestaBuilder();
        richiesta.costruisciTipologia(tipologia);
        richiesta.costruisciIdInformazioni(idContenuto);

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

    
    public Richiesta valutaRichiesta(Richiesta richiesta, Boolean stato) {
        richiesta.setApprovazione(stato);
        return this.salvaRichiesta(richiesta);
    }

}
