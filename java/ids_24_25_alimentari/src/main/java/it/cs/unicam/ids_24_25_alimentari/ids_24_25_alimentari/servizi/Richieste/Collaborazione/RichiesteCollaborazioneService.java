package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Collaborazione;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione.CollaborazioneOutDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione.RichiesteCollaborazioneOutputDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.Richieste.RichiestaCollaborazioneBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.Contenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.Collaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.RichiestaCollaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaCollaborazioneRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.AziendaService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.CollaborazioneService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.RichiestaService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.UtenteService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp.ImplementazioneServizioMail;
import org.apache.commons.lang3.RandomStringUtils;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp.ServizioEmail;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RichiesteCollaborazioneService extends RichiestaService {
    private final RichiestaCollaborazioneRepository collaborazioneRepository;
    private final AziendaService aziendaService;
    private final CollaborazioneService collaborazioneService;
    private final ServizioEmail servizioEmail;
    public RichiesteCollaborazioneService(RichiestaCollaborazioneRepository collaborazioneRepository, UtenteService utenteService, CollaborazioneService collaborazioneService, ServizioEmail servizioEmail, ImplementazioneServizioMail implementazioneServizioMail, AziendaService aziendaService) {
        super(implementazioneServizioMail, utenteService);
        this.collaborazioneRepository = collaborazioneRepository;
        this.collaborazioneService = collaborazioneService;
        this.servizioEmail = servizioEmail;
        this.aziendaService = aziendaService;
    }

/**
 * <h2>Recupera tutte le richieste di collaborazione</h2>
 * <br>
 * Questo metodo restituisce tutte le richieste di collaborazione presenti nel database,
 * ordinate in base al criterio specificato.
 *
 * @param sortBy Il criterio di ordinamento: "ruolo", "stato" o "id".
 * @return Una lista di oggetti `RichiesteCollaborazioneOutputDTO` che rappresentano le richieste di collaborazione.
 */
public List<RichiesteCollaborazioneOutputDTO> getAllRichieste(String sortBy) {
   List<RichiestaCollaborazione> richieste = new ArrayList<>(collaborazioneRepository.findAll());
   Comparator<RichiestaCollaborazione> comparator = switch (sortBy.toLowerCase()) {
       case "ruolo" -> Comparator.comparing(RichiestaCollaborazione::getRuolo);
       case "stato" -> Comparator.comparing(RichiestaCollaborazione::getApprovato);
       default -> Comparator.comparingLong(RichiestaCollaborazione::getId);
   };
   return richieste.stream()
           .sorted(comparator)
           .map(RichiesteCollaborazioneOutputDTO::new)
           .collect(Collectors.toList());
}

    /**
     * <h2>Recupera una richiesta di collaborazione tramite ID</h2>
     * <br>
     * Questo metodo restituisce una richiesta di collaborazione specifica
     * identificata dal suo ID, se presente nel database.
     *
     * @param id L'ID della richiesta di collaborazione da recuperare.
     * @return Un oggetto `Optional<CollaborazioneOutDTO>` che rappresenta la richiesta di collaborazione,
     *         oppure un `Optional` vuoto se la richiesta non è stata trovata.
     */
    public ResponseEntity<?> ottieniRichiestaById(long id) {
        RichiestaCollaborazione collaborazione = getRichiestaById(id);
        if (collaborazione == null) {
            return
            ResponseEntity.status(404)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"Richiesta non trovata\"}");
        } else {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Optional.of(collaborazione).map(CollaborazioneOutDTO::new));
        }
    }

    public RichiestaCollaborazione getRichiestaById(long id) {
        return collaborazioneRepository.findById(id).orElse(null);
    }

    /**
     * <h2>Salva una richiesta di collaborazione</h2>
     * <br>
     * Questo metodo salva una richiesta di collaborazione nel database.
     *
     * @param richiesta La richiesta di collaborazione da salvare.
     * @return La richiesta di collaborazione salvata.
     */
    public RichiestaCollaborazione salvaRichiesta(RichiestaCollaborazione richiesta) {
        return collaborazioneRepository.save(richiesta);
    }


    public void deleteRichiesta(long id) {
        collaborazioneRepository.deleteById(id);
    }


    public RichiestaCollaborazione creaRichiesta(Contenuto contenuto, Ruolo ruolo){
        RichiestaCollaborazioneBuilder builder = new RichiestaCollaborazioneBuilder();
        builder.costruisciRuolo(ruolo);
        builder.costruisciCollaborazione((Collaborazione) contenuto);
        return builder.build();
    }

/**
 * <h2>Crea una richiesta di collaborazione per un'azienda</h2>
 * <br>
 * Questo metodo consente di creare una nuova richiesta di collaborazione per un'azienda.
 * La richiesta viene notificata ai destinatari e salvata nel database.
 *
 * @param nome          Nome del rappresentante dell'azienda.
 * @param cognome       Cognome del rappresentante dell'azienda.
 * @param telefono      Numero di telefono del rappresentante.
 * @param email         Indirizzo email del rappresentante.
 * @param ruolo         Ruolo richiesto per la collaborazione.
 * @param denSociale    Denominazione sociale dell'azienda.
 * @param sedeLegale    Indirizzo della sede legale dell'azienda.
 * @param sedeOperativa Indirizzo della sede operativa dell'azienda.
 * @param iban          IBAN dell'azienda.
 * @param iva           Partita IVA dell'azienda.
 * @param certificato   File contenente il certificato dell'azienda.
 * @param cartaIdentita File contenente la carta d'identità del rappresentante.
 * @return La richiesta di collaborazione creata e salvata nel database.
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
        File cartaIdentita  )
{
    Collaborazione collab = collaborazioneService.creaNuovaAzienda(
            nome,
            cognome,
            telefono,
            email,
            ruolo,
            denSociale,
            sedeLegale,
            sedeOperativa,
            iban,
            iva,
            certificato,
            cartaIdentita
    );
    return completaRichiesta(collab, ruolo);
}

/**
 * <h2>Crea una richiesta di collaborazione per un animatore</h2>
 * <br>
 * Questo metodo consente di creare una nuova richiesta di collaborazione per un animatore.
 * La richiesta viene notificata ai destinatari e salvata nel database.
 *
 * @param nome          Nome dell'animatore.
 * @param cognome       Cognome dell'animatore.
 * @param telefono      Numero di telefono dell'animatore.
 * @param email         Indirizzo email dell'animatore.
 * @param ruolo         Ruolo richiesto per la collaborazione.
 * @param iban          IBAN dell'animatore.
 * @param cartaIdentita File contenente la carta d'identità dell'animatore.
 * @return La richiesta di collaborazione creata e salvata nel database.
 */
public RichiestaCollaborazione creaRichiestaAnimatore(
        String nome,
        String cognome,
        String telefono,
        String email,
        Ruolo ruolo,
        String iban,
        File cartaIdentita  )
{
    Collaborazione collab = collaborazioneService.creaNuovoAnimatore(
            nome,
            cognome,
            telefono,
            email,
            ruolo,
            iban,
            cartaIdentita
    );
    return completaRichiesta(collab, ruolo);
}

/**
 * <h2>Crea una richiesta di collaborazione per un curatore</h2>
 * <br>
 * Questo metodo consente di creare una nuova richiesta di collaborazione per un curatore.
 * La richiesta viene notificata ai destinatari e salvata nel database.
 *
 * @param nome          Nome del curatore.
 * @param cognome       Cognome del curatore.
 * @param telefono      Numero di telefono del curatore.
 * @param email         Indirizzo email del curatore.
 * @param ruolo         Ruolo richiesto per la collaborazione.
 * @param iban          IBAN del curatore.
 * @param cartaIdentita File contenente la carta d'identità del curatore.
 * @param cv            File contenente il curriculum vitae del curatore.
 * @return La richiesta di collaborazione creata e salvata nel database.
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

    Collaborazione collab = collaborazioneService.creaNuovoCuratore(
            nome,
            cognome,
            telefono,
            email,
            ruolo,
            iban,
            cartaIdentita,
            cv
    );
    return completaRichiesta(collab, ruolo);
}

    private RichiestaCollaborazione completaRichiesta(Collaborazione collab, Ruolo ruolo) {
        RichiestaCollaborazione richiesta = creaRichiesta(collab, ruolo);
        notificaNuovaRichiesta(Ruolo.GESTORE);
        return salvaRichiesta(richiesta);
    }

    public ResponseEntity<RichiestaCollaborazione> accettaRichiesta(long id) {
        Optional<RichiestaCollaborazione> richiesta = collaborazioneRepository.findById(id);
        if (richiesta.isPresent()) {
            richiesta.get().setApprovato(true);
            if (richiesta.get().getApprovato()) {
                Optional<Collaborazione> collab = collaborazioneService.getCollabById(richiesta.get().getCollaborazione().getId());
                processaCollaborazione(collab);
            }
            return ResponseEntity.ok(salvaRichiesta(richiesta.get()));
        }
            else return ResponseEntity.notFound().build();
    }

    private void processaCollaborazione(Optional<Collaborazione> collab) {
        String password = RandomStringUtils.randomAlphanumeric(8);
        switch(collab.get().getRuolo()){
            case PRODUTTORE,TRASFORMATORE,DISTRIBUTORE -> {
                utenteService.nuovoAzienda(
                        collab.get().getNome(),
                        collab.get().getCognome(),
                        collab.get().getEmail(),
                        password,
                        collab.get().getTelefono(),
                        collab.get().getRuolo(),
                        collab.get().getIban(),
                        generaAzienda(collab),
                        collab.get().getCartaIdentita()

                );
            }
            case ANIMATORE -> {
                utenteService.nuovoAnimatore(
                        collab.get().getNome(),
                        collab.get().getCognome(),
                        collab.get().getEmail(),
                        password,
                        collab.get().getTelefono(),
                        collab.get().getIban(),
                        collab.get().getCartaIdentita()
                );
            }
            case CURATORE -> {
                utenteService.nuovoCuratore(
                        collab.get().getNome(),
                        collab.get().getCognome(),
                        collab.get().getEmail(),
                        password,
                        collab.get().getTelefono(),
                        collab.get().getIban(),
                        collab.get().getCartaIdentita(),
                        collab.get().getCv()
                );
            }
            case GESTORE -> {
                // Gestore non ha bisogno di un account, quindi non facciamo nulla
            }
        }
        inviaConferma(collab.get().getRuolo(), collab.get().getEmail(), password);
    }

    private Long generaAzienda(Optional<Collaborazione> collaborazione) {
        Azienda azienda = aziendaService.createAzienda(
                collaborazione.get().getDenominazioneSociale(),
                collaborazione.get().getSedeLegale(),
                collaborazione.get().getSedeOperativa(),
                collaborazione.get().getIva(),
                collaborazione.get().getCertificato());
        return azienda.getId();
    }

    private void inviaConferma(Ruolo ruolo, String email, String password){
        String messaggio = "La sua richiesta di collaborazione per il ruolo di "
                + ruolo
                + " è stata accettata con successo! Ecco le sue credenziali:\n"
                + "Email: " + email + "\n" + "Password: " + password;
        this.servizioEmail.inviaMail(email, messaggio,
                "Accettazione Richiesta di Collaborazione");
    }


}
