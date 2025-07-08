package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Collaborazione;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione.CollaborazioneAnimatoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione.CollaborazioneOutDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione.RichiesteCollaborazioneOutputDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.ValutaRichiestaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.Richieste.RichiestaCollaborazioneBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.Contenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.Collaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.RichiestaCollaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RichiesteCollaborazioneService extends RichiestaService {
    private final RichiestaCollaborazioneRepository richiestaRepository;
    private final AziendaService aziendaService;
    private final CollaborazioneService collaborazioneService;
    private final ServizioEmail servizioEmail;


    public RichiesteCollaborazioneService(RichiestaCollaborazioneRepository richiestaRepository,
            UtenteService utenteService, CollaborazioneService collaborazioneService, ServizioEmail servizioEmail,
            ImplementazioneServizioMail implementazioneServizioMail, AziendaService aziendaService) {
        super(implementazioneServizioMail, utenteService);
        this.richiestaRepository = richiestaRepository;
        this.collaborazioneService = collaborazioneService;
        this.servizioEmail = servizioEmail;
        this.aziendaService = aziendaService;
    }

    /**
     * <h2>Recupera tutte le richieste di collaborazione</h2>
     * <br>
     * Questo metodo restituisce tutte le richieste di collaborazione presenti nel
     * database,
     * ordinate in base al criterio specificato.
     *
     * @param sortBy Il criterio di ordinamento: "ruolo", "stato" o "id".
     * @return Una lista di oggetti `RichiesteCollaborazioneOutputDTO` che
     *         rappresentano le richieste di collaborazione.
     */
    public List<RichiesteCollaborazioneOutputDTO> getAllRichieste(String sortBy) {

        List<RichiestaCollaborazione> richieste = new ArrayList<>(richiestaRepository.findAll());
        if (sortBy == null || sortBy.isEmpty()) {
            return richieste.stream()
                    .map(RichiesteCollaborazioneOutputDTO::new)
                    .collect(Collectors.toList());
        }

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
     * @return Un oggetto `Optional<CollaborazioneOutDTO>` che rappresenta la
     *         richiesta di collaborazione,
     *         oppure un `Optional` vuoto se la richiesta non è stata trovata.
     */
    public ResponseEntity<?> getCollaborazioneById(long id) {
        RichiestaCollaborazione collaborazione = getRichiestaById(id);

        try {
            Ruolo ruolo = collaborazione.getRuolo();
            if (ruolo == Ruolo.ANIMATORE){
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Optional.of(collaborazione).map(CollaborazioneAnimatoreDTO::new));
            }

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Optional.of(collaborazione).map(CollaborazioneOutDTO::new));
        } catch (NullPointerException e) {
            return ResponseEntity.status(404)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"Richiesta non trovata\"}");
        }
    }


    public RichiestaCollaborazione getRichiestaById(long id) {
        return richiestaRepository.findById(id).orElse(null);
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
        return richiestaRepository.save(richiesta);
    }


    /**
     * <h2>Elimina una richiesta di collaborazione</h2>
     * <br>
     * Questo metodo elimina una richiesta di collaborazione specificata dal suo ID.
     *
     * @param id L'ID della richiesta di collaborazione da eliminare.
     * @return Una risposta HTTP che indica il successo o il fallimento dell'operazione:
     *        - 200 OK se la richiesta è stata eliminata con successo.
     *        - 404 NOT FOUND se la richiesta non esiste.
     */
    public ResponseEntity<?> eliminaRichiesta(long id) {
        Optional<RichiestaCollaborazione> collaborazione = richiestaRepository.findById(id);
        if (collaborazione.isPresent()) {
            deleteCollaborazione(collaborazione.get());
            deleteRichiesta(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Richiesta eliminata con successo"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"Richiesta non trovata\"}");
        }
    }
    private void deleteRichiesta(long id) {
        richiestaRepository.deleteById(id);
    }

    private void deleteCollaborazione(RichiestaCollaborazione richiestaCollaborazione){
        Collaborazione collaborazione = richiestaCollaborazione.getCollaborazione();
        if (collaborazione!=null) {
            richiestaRepository.deleteById(collaborazione.getId());
        } else {
            throw new NoSuchElementException("Collaborazione non trovata");
        }
    }


    public RichiestaCollaborazione creaRichiesta(Contenuto contenuto, Ruolo ruolo) {
        RichiestaCollaborazioneBuilder builder = new RichiestaCollaborazioneBuilder();
        builder.costruisciRuolo(ruolo);
        builder.costruisciCollaborazione((Collaborazione) contenuto);
        return builder.build();
    }

    /**
     * <h2>Crea una richiesta di collaborazione per un'azienda</h2>
     * <br>
     * Questo metodo consente di creare una nuova richiesta di collaborazione per
     * un'azienda.
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
            File cartaIdentita) {

        if (sedeOperativa == null) {
            sedeOperativa = new Indirizzo(
                    sedeLegale.getVia(),
                    sedeLegale.getNumeroCivico(),
                    sedeLegale.getCitta(),
                    sedeLegale.getProvincia(),
                    sedeLegale.getCap());
        }

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
                cartaIdentita);
        return completaRichiesta(collab, ruolo);
    }

    /**
     * <h2>Crea una richiesta di collaborazione per un animatore</h2>
     * <br>
     * Questo metodo consente di creare una nuova richiesta di collaborazione per un
     * animatore.
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
            File cartaIdentita) {
        Collaborazione collab = collaborazioneService.creaNuovoAnimatore(
                nome,
                cognome,
                telefono,
                email,
                ruolo,
                iban,
                cartaIdentita);
        return completaRichiesta(collab, ruolo);
    }

    /**
     * <h2>Crea una richiesta di collaborazione per un curatore</h2>
     * <br>
     * Questo metodo consente di creare una nuova richiesta di collaborazione per un
     * curatore.
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
                cv);
        return completaRichiesta(collab, ruolo);
    }

    private RichiestaCollaborazione completaRichiesta(Collaborazione collab, Ruolo ruolo) {
        RichiestaCollaborazione richiesta = creaRichiesta(collab, ruolo);
        notificaNuovaRichiesta(Ruolo.GESTORE);
        return salvaRichiesta(richiesta);
    }

    private void processaCollaborazione(Optional<Collaborazione> collab) {
        String password = RandomStringUtils.randomAlphanumeric(8);
        Collaborazione collaborazione = collab.orElseThrow(() -> new NoSuchElementException("Collaborazione non trovata"));
            Utente utente = utenteService.nuovoUtente(
                    collaborazione.getNome(),
                    collaborazione.getCognome(),
                    collaborazione.getEmail(),
                    password,
                    collaborazione.getTelefono(),
                    collaborazione.getIban(),
                    collaborazione.getRuolo(),
                    collaborazione.getCartaIdentita(),
                    collaborazione.getCv());
        if (utente.getRuolo()==Ruolo.PRODUTTORE || utente.getRuolo()==Ruolo.TRASFORMATORE || utente.getRuolo()==Ruolo.DISTRIBUTORE)
            generaAzienda(collaborazione, utente);
        String messaggio = "La sua richiesta di collaborazione con id " + collaborazione.getId() + " per il ruolo di "
                + collaborazione.getRuolo()
                + " è stata accettata con successo! Ecco le sue credenziali:\n"
                + "Email: " + collaborazione.getEmail() + "\n" + "Password: " + password;
        notificaAccettazioneRichiesta(messaggio, collaborazione.getEmail(), "Collaborazione");
    }

    private void generaAzienda(Collaborazione collaborazione,Utente utente) {
        Azienda azienda = aziendaService.createAzienda(
                collaborazione.getDenominazioneSociale(),
                collaborazione.getSedeLegale(),
                collaborazione.getSedeOperativa(),
                collaborazione.getIva(),
                collaborazione.getCertificato(),
                utente
                );
        aziendaService.saveAzienda(azienda);
    }

    public ResponseEntity<?> elaboraRichiesta(ValutaRichiestaDTO dto, long id) {
        RichiestaCollaborazione richiesta = getRichiestaById(id);
        if (richiesta!=null) {
            if (richiesta.getApprovato() != null) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("message", "La richiesta è già stata elaborata"));
            }

            if (dto.getStato()) {
               accettaRichiesta(richiesta);
            } else {
                if (dto.getMessaggioAggiuntivo() == null) {
                    return ResponseEntity.badRequest()
                            .body(Collections.singletonMap("message", "Inserire un messaggio di rifiuto"));
                }
                rifiutaRichiesta(richiesta, dto);
            }
            return ResponseEntity.ok().body(Collections.singletonMap("message",
                    dto.getStato() ? "Richiesta accettata con successo." : "Richiesta correttamente rifiutata."));
        }
        return ResponseEntity.status(404).body(Collections.singletonMap("error", " Richiesta non trovata"));
    }

    public ResponseEntity<?> accettaRichiesta(RichiestaCollaborazione richiesta) {
        try{
            richiesta.setApprovato(true);
            Optional<Collaborazione> collab = collaborazioneService
                    .getCollabById(richiesta.getCollaborazione().getId());
            processaCollaborazione(collab);
            return ResponseEntity.ok(salvaRichiesta(richiesta));
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e);
        }
    }

    public ResponseEntity<?> rifiutaRichiesta(RichiestaCollaborazione richiesta, ValutaRichiestaDTO dto) {
        try{
            richiesta.setApprovato(false);
            Optional<Collaborazione> collab = collaborazioneService
                    .getCollabById(richiesta.getCollaborazione().getId());
            collab.ifPresent(collaborazione -> collaborazioneService.deleteCollaborazione(collaborazione.getId()));
            // notifica il rifiuto all'utente tramite mail
            notificaRifiutoRichiesta(dto.getMessaggioAggiuntivo(), richiesta.getCollaborazione().getEmail(), "collaborazione");
            return ResponseEntity.ok(salvaRichiesta(richiesta));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e);
        }
    }

}
