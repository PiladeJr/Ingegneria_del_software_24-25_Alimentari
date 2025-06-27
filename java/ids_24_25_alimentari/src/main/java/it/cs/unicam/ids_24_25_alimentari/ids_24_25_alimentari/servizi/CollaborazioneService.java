package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.Collaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.CollaborazioneDirector;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.CollaborazioneRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
public class CollaborazioneService {
    private final CollaborazioneRepository collaborazioneRepository;

    public CollaborazioneService(CollaborazioneRepository collaborazioneRepository) {
        this.collaborazioneRepository = collaborazioneRepository;
    }

    /**
     * <h2>Ottieni una collaborazione per ID</h2>
     * <br>
     * Questo metodo restituisce un oggetto di tipo Optional<Collaborazione> che
     * rappresenta
     * una collaborazione identificata dall'ID specificato.
     *
     * @param id L'ID della collaborazione da cercare.
     * @return Un Optional contenente la collaborazione, se trovata; altrimenti un
     *         Optional vuoto.
     */
    public Optional<Collaborazione> getCollabById(long id) {
        return collaborazioneRepository.findById(id);
    }

    /**
     * <h2>Crea una nuova Azienda</h2>
     * <br>
     * Questo metodo crea una nuova collaborazione per un'Azienda e la salva nel
     * database.
     * Utilizza il `CollaborazioneDirector` per costruire l'oggetto
     * `Collaborazione`.
     *
     * @param nome          Il nome del rappresentante dell'Azienda.
     * @param cognome       Il cognome del rappresentante dell'Azienda.
     * @param telefono      Il numero di telefono del rappresentante dell'Azienda.
     * @param email         L'email del rappresentante dell'Azienda.
     * @param ruolo         Il ruolo del rappresentante dell'Azienda.
     * @param denSociale    La denominazione sociale dell'Azienda.
     * @param sedeLegale    L'indirizzo della sede legale dell'Azienda.
     * @param sedeOperativa L'indirizzo della sede operativa dell'Azienda.
     * @param iban          L'IBAN dell'Azienda.
     * @param iva           La partita IVA dell'Azienda.
     * @param certificato   Il file del certificato dell'Azienda.
     * @param cartaIdentita Il file della carta d'identità del rappresentante.
     * @return L'ID della collaborazione appena creata.
     */
    public Collaborazione creaNuovaAzienda(
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
        CollaborazioneDirector director = new CollaborazioneDirector();
        Collaborazione collaborazione = director.creaAzienda(nome, cognome, telefono, email, ruolo, denSociale,
                sedeLegale, sedeOperativa, iban, iva, certificato, cartaIdentita);
        return collaborazioneRepository.save(collaborazione);
    }

    /**
     * <h2>Crea un nuovo Animatore</h2>
     * <br>
     * Questo metodo crea una nuova collaborazione per un Animatore e la salva nel
     * database.
     * Utilizza il `CollaborazioneDirector` per costruire l'oggetto
     * `Collaborazione`.
     *
     * @param nome          Il nome dell'Animatore.
     * @param cognome       Il cognome dell'Animatore.
     * @param telefono      Il numero di telefono dell'Animatore.
     * @param email         L'email dell'Animatore.
     * @param ruolo         Il ruolo dell'Animatore.
     * @param iban          L'IBAN dell'Animatore.
     * @param cartaIdentita Il file della carta d'identità dell'Animatore.
     * @return L'ID della collaborazione appena creata.
     */
    public Collaborazione creaNuovoAnimatore(
            String nome,
            String cognome,
            String telefono,
            String email,
            Ruolo ruolo,
            String iban,
            File cartaIdentita) {
        CollaborazioneDirector director = new CollaborazioneDirector();
        Collaborazione collaborazione = director.creaAnimatore(nome, cognome, telefono, email, ruolo, iban,
                cartaIdentita);
        return collaborazioneRepository.save(collaborazione);
    }

    /**
     * <h2>Crea un nuovo Curatore</h2>
     * <br>
     * Questo metodo crea una nuova collaborazione per un Curatore e la salva nel
     * database.
     * Utilizza il `CollaborazioneDirector` per costruire l'oggetto
     * `Collaborazione`.
     *
     * @param nome          Il nome del Curatore.
     * @param cognome       Il cognome del Curatore.
     * @param telefono      Il numero di telefono del Curatore.
     * @param email         L'email del Curatore.
     * @param ruolo         Il ruolo del Curatore.
     * @param iban          L'IBAN del Curatore.
     * @param cartaIdentita Il file della carta d'identità del Curatore.
     * @param cv            Il file del curriculum vitae del Curatore.
     * @return L'ID della collaborazione appena creata.
     */
    public Collaborazione creaNuovoCuratore(
            String nome,
            String cognome,
            String telefono,
            String email,
            Ruolo ruolo,
            String iban,
            File cartaIdentita,
            File cv) {
        CollaborazioneDirector director = new CollaborazioneDirector();
        Collaborazione collaborazione = director.creaCuratore(nome, cognome, telefono, email, ruolo, iban,
                cartaIdentita, cv);
        return collaborazioneRepository.save(collaborazione);
    }

    /**
     * <h2>Salva una collaborazione</h2>
     * <br>
     * Questo metodo salva un oggetto di tipo Collaborazione nel database.
     * Se la collaborazione fornita è null, viene lanciata un'eccezione
     * IllegalArgumentException.
     *
     * @param collaborazione L'oggetto Collaborazione da salvare.
     * @throws IllegalArgumentException Se la collaborazione fornita è null.
     */
    public void salvaCollaborazione(Collaborazione collaborazione) {
        if (collaborazione != null) {
            collaborazioneRepository.save(collaborazione);
        } else {
            throw new IllegalArgumentException("La collaborazione non può essere null");
        }
    }

    /**
     * <h2>Elimina una collaborazione</h2>
     * <br>
     * Questo metodo elimina una collaborazione dal database utilizzando il suo ID.
     *
     * @param idCollaborazione L'ID della collaborazione da eliminare.
     */
    public void deleteCollaborazione(Long idCollaborazione) {
        collaborazioneRepository.deleteById(idCollaborazione);
    }
}
