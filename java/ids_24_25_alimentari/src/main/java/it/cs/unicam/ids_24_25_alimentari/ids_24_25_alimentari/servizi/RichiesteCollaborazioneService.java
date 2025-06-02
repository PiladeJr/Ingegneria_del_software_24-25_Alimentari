package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import java.util.List;
import java.util.Optional;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesteCollaborazione.RichiestaCollaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesteCollaborazione.RichiestaCollaborazioneDirector;
import org.apache.commons.lang3.RandomStringUtils;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp.ServizioEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaCollaborazioneRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.RichiestaCollaborazioneBuilder;
import java.io.File;

@Service
public class RichiesteCollaborazioneService {
    @Autowired
    private RichiestaCollaborazioneRepository richiestaCollaborazioneRepository;
    @Autowired
    private AziendaService aziendaService;
    @Autowired
    private final UtenteService utenteService;
    @Autowired
    private final ServizioEmail servizioEmail;

    public RichiesteCollaborazioneService(UtenteService utenteService, ServizioEmail servizioEmail) {
        this.utenteService = utenteService;
        this.servizioEmail = servizioEmail;
    }

    public List<RichiestaCollaborazione> getAllRichieste() {
        return richiestaCollaborazioneRepository.findAll();
    }

    public Optional<RichiestaCollaborazione> getRichiestaById(long id) {
        return richiestaCollaborazioneRepository.findById(id);
    }

    public RichiestaCollaborazione saveRichiesta(RichiestaCollaborazione richiesta) {
        return richiestaCollaborazioneRepository.save(richiesta);
    }

    public void deleteRichiesta(long id) {
        richiestaCollaborazioneRepository.deleteById(id);
    }

    /**
     * <h2>Crea una richiesta di collaborazione per un'azienda</h2>
     * <br>
     * Questo metodo costruisce e salva una richiesta di collaborazione per
     * un'azienda,
     * utilizzando il pattern Builder e un Director per orchestrare la creazione
     * dell'oggetto.
     *
     * @param nome          Il nome del rappresentante dell'azienda.
     * @param cognome       Il cognome del rappresentante dell'azienda.
     * @param telefono      Il numero di telefono del rappresentante.
     * @param email         L'indirizzo email del rappresentante.
     * @param ruolo         Il ruolo PRODUTTORE, TRASFORMATORE O DISTRIBUTORE.
     * @param denSociale    La denominazione sociale dell'azienda.
     * @param sedeLegale    L'indirizzo della sede legale dell'azienda.
     * @param sedeOperativa L'indirizzo della sede operativa dell'azienda.
     * @param iban          Il codice IBAN dell'azienda.
     * @param iva           Il numero di partita IVA dell'azienda.
     * @param certificato   Il file contenente il certificato dell'azienda.
     * @param cartaIdentita Il file della carta d'identità del rappresentante.
     * @return L'oggetto {@code RichiestaCollaborazione} creato e salvato nel
     *         sistema.
     */
    public RichiestaCollaborazione creaRichiestaAzienda(
            String nome,
            String cognome,
            String telefono,
            String email,
            Ruolo ruolo,
            String denSociale,
            Indirizzo sedeLegale,
            Indirizzo sedeOperativa,
            String iban,
            String iva,
            File certificato,
            File cartaIdentita) {
        if (utenteService.isRegistrato(email)) {
            throw new IllegalArgumentException("Utente già registrato");
        }
        RichiestaCollaborazioneBuilder builder = new RichiestaCollaborazioneBuilder();
        RichiestaCollaborazioneDirector director = new RichiestaCollaborazioneDirector(builder);
        director.creaAzienda(nome, cognome, telefono, email, ruolo, denSociale, sedeLegale, sedeOperativa, iban, iva,
                certificato, cartaIdentita);
        return saveRichiesta(builder.getRichiesta());
    }

    /**
     * <h2>Crea una richiesta di collaborazione per un animatore</h2>
     * <br>
     *
     * @param nome             Il nome dell'animatore
     * @param cognome          Il cognome dell'animatore
     * @param telefono         Il numero di telefono dell'animatore
     * @param email            L'email dell'animatore
     * @param ruolo            Il ruolo ANIMATORE
     * @param aziendaReferente L'azienda referente dell'animatore
     * @param iban             L'IBAN dell'animatore
     * @param cartaIdentita    Il file della carta d'identità dell'animatore
     * @return La richiesta di collaborazione creata e salvata
     */
    public RichiestaCollaborazione creaRichiestaAnimatore(
            String nome,
            String cognome,
            String telefono,
            String email,
            Ruolo ruolo,
            String aziendaReferente,
            String iban,
            File cartaIdentita) {
        if (utenteService.isRegistrato(email)) {
            throw new IllegalArgumentException("Utente già registrato");
        }
        RichiestaCollaborazioneBuilder builder = new RichiestaCollaborazioneBuilder();
        RichiestaCollaborazioneDirector director = new RichiestaCollaborazioneDirector(builder);
        director.creaAnimatore(nome, cognome, telefono, email, ruolo, iban, cartaIdentita);
        return saveRichiesta(builder.getRichiesta());
    }

    /**
     * <h2>Crea una richiesta di collaborazione per un curatore</h2>
     * <br>
     *
     * @param nome          Il nome del curatore
     * @param cognome       Il cognome del curatore
     * @param telefono      Il numero di telefono del curatore
     * @param email         L'email del curatore
     * @param ruolo         Il ruolo CURATORE
     * @param iban          L'IBAN del curatore
     * @param cartaIdentita Il file della carta d'identità del curatore
     * @param cv            Il file del curriculum vitae del curatore
     * @return La richiesta di collaborazione creata e salvata
     */
    public RichiestaCollaborazione creaRichiestaCuratore(
            String nome,
            String cognome,
            String telefono,
            String email,
            Ruolo ruolo,
            String iban,
            File cartaIdentita,
            File cv) {
        if (utenteService.isRegistrato(email)) {
            throw new IllegalArgumentException("Utente già registrato");
        }
        RichiestaCollaborazioneBuilder builder = new RichiestaCollaborazioneBuilder();
        RichiestaCollaborazioneDirector director = new RichiestaCollaborazioneDirector(builder);
        director.creaCuratore(nome, cognome, telefono, email, ruolo, iban, cartaIdentita, cv);
        return saveRichiesta(builder.getRichiesta());
    }

    /**
     * <h2>Imposta lo stato di una richiesta di collaborazione</h2>
     * <br>
     * Questo metodo aggiorna lo stato di una richiesta di collaborazione.
     * Se lo stato viene impostato su "accettato" (true), viene generato
     * automaticamente un account per l'utente.
     *
     * @param id    L'ID della richiesta di collaborazione da aggiornare.
     * @param stato Il nuovo stato della richiesta (true = accettata, false =
     *              rifiutata).
     * @return La richiesta di collaborazione aggiornata, o null se non trovata.
     */
    public RichiestaCollaborazione setStato(long id, boolean stato) {
        Optional<RichiestaCollaborazione> richiesta = getRichiestaById(id);
        if (richiesta.isPresent()) {
            richiesta.get().setStato(stato);
            if (stato) {
                generaAccount(id);
            }
            return saveRichiesta(richiesta.get());
        }
        return null;
    }

    /**
     * <h2>Genera un account per un nuovo utente</h2>
     * <br>
     * Questo metodo crea un nuovo account per un utente che ha presentato
     * una richiesta di collaborazione. Se il ruolo dell'utente è
     * un'azienda (Produttore, Trasformatore, Distributore), viene
     * prima creata l'azienda e successivamente l'utente associato.
     * Una password casuale viene generata e inviata via email.
     *
     * @param id L'ID della richiesta di collaborazione da elaborare.
     */
    public void generaAccount(long id) {
        Optional<RichiestaCollaborazione> richiesta = getRichiestaById(id);
        if (richiesta.isPresent()) {
            RichiestaCollaborazione richiestaCollaborazione = richiesta.get();
            switch (richiestaCollaborazione.getRuolo()) {
                case PRODUTTORE, TRASFORMATORE, DISTRIBUTORE -> {
                    Azienda azienda = aziendaService.createAzienda(
                            richiestaCollaborazione.getDenominazioneSociale(),
                            richiestaCollaborazione.getSedeLegale(),
                            richiestaCollaborazione.getSedeOperativa(),
                            richiestaCollaborazione.getIva(),
                            richiestaCollaborazione.getIban(),
                            richiestaCollaborazione.getCertificato());

                    String password = RandomStringUtils.randomAlphanumeric(8);
                    // crea un utente che ha un'azienda
                    utenteService.nuovoAzienda(
                            richiestaCollaborazione.getNome(),
                            richiestaCollaborazione.getCognome(),
                            richiestaCollaborazione.getEmail(),
                            richiestaCollaborazione.getTelefono(),
                            richiestaCollaborazione.getRuolo(),
                            azienda.getId(),
                            richiestaCollaborazione.getCartaIdentita(),
                            password);

                    String messaggio = "La sua richiesta di collaborazione per il ruolo di "
                            + richiestaCollaborazione.getRuolo()
                            + " è stata accettata con successo! Ecco le sue credenziali:\n"
                            + "Email: " + richiestaCollaborazione.getEmail() + "\n" + "Password: " + password;
                    this.servizioEmail.inviaMail(richiestaCollaborazione.getEmail(), messaggio,
                            "Accettazione Richiesta di Collaborazione");
                }
                case ANIMATORE -> {
                    String password = RandomStringUtils.randomAlphanumeric(8);
                    // crea un utente che è un animatore
                    utenteService.nuovoAnimatore(
                            richiestaCollaborazione.getNome(),
                            richiestaCollaborazione.getCognome(),
                            richiestaCollaborazione.getEmail(),
                            password,
                            richiestaCollaborazione.getTelefono(),
                            richiestaCollaborazione.getIban(),
                            richiestaCollaborazione.getCartaIdentita());

                    String messaggio = "La sua richiesta di collaborazione per il ruolo di Animatore è stata accettata con successo! Ecco le sue credenziali:\n"
                            + "Email: " + richiestaCollaborazione.getEmail() + "\n" + "Password: " + password;
                    this.servizioEmail.inviaMail(richiestaCollaborazione.getEmail(), messaggio,
                            "Accettazione Richiesta di Collaborazione");
                }
                case CURATORE -> {
                    String password = RandomStringUtils.randomAlphanumeric(8);
                    // crea un utente che è un curatore
                    utenteService.nuovoCuratore(
                            richiestaCollaborazione.getNome(),
                            richiestaCollaborazione.getCognome(),
                            richiestaCollaborazione.getEmail(),
                            password,
                            richiestaCollaborazione.getTelefono(),
                            richiestaCollaborazione.getIban(),
                            richiestaCollaborazione.getCartaIdentita(),
                            richiestaCollaborazione.getCurriculum());

                    String messaggio = "La sua richiesta di collaborazione per il ruolo di Curatore è stata accettata con successo! Ecco le sue credenziali:\n"
                            + "Email: " + richiestaCollaborazione.getEmail() + "\n" + "Password: " + password;
                    this.servizioEmail.inviaMail(richiestaCollaborazione.getEmail(), messaggio,
                            "Accettazione Richiesta di Collaborazione");
                }
                case GESTORE -> {
                }
            }

        }
    }
}
