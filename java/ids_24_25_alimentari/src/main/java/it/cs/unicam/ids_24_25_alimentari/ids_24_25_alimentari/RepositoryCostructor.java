package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.RichiestaInformazioniAggiuntive;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.AziendaRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.IndirizzoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaCollaborazioneRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaInformazioniAggiuntiveRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.UtenteRepository;
import jakarta.annotation.PostConstruct;

@Component
public class RepositoryCostructor {

    @Autowired
    private AziendaRepository aziendaRepository;

    @Autowired
    private IndirizzoRepository indirizzoRepository;

    @Autowired
    private RichiestaRepository richiestaRepository;

    @Autowired
    private RichiestaCollaborazioneRepository richiestaCollaborazioneRepository;

    @Autowired
    private RichiestaInformazioniAggiuntiveRepository richiestaInformazioniAggiuntiveRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @PostConstruct
    public void init() {
        impostaUtenti(utenteRepository);
        impostaAziende(aziendaRepository);
        impostaRichiesteInformazioniAggiuntive(richiestaInformazioniAggiuntiveRepository);
        impostaRichiesteCollaborazione(richiestaCollaborazioneRepository);
        impostaRichieste(richiestaRepository);
        impostaIndirizzi(indirizzoRepository);
    }

    public boolean isRichiestaInformazioniAggiuntiveRepositorySet = false;
    public boolean isIndirizzoRepositorySet = false;
    public boolean isRichiestaRepositorySet = false;
    public boolean isRichiestaCollaborazioneRepositorySet = false;
    public boolean isUtenteRepositorySet = false;
    public boolean isAziendaRepositorySet = false;

    public Utente PRODUTTORE,
            TRASFORMATORE,
            DISTRIBUTORE,
            ANIMATORE,
            ACQUIRENTE,
            CURATORE,
            GESTORE;

    public Azienda AZIENDA_PRODUTTORE,
            AZIENDA_TRASFORMATORE,
            AZIENDA_DISTRIBUTORE;

    public Indirizzo INDIRIZZO_PRODUTTORE,
            INDIRIZZO_TRASFORMATORE,
            INDIRIZZO_DISTRIBUTORE;

    public RichiestaInformazioniAggiuntive RICHIESTA_INFORMAZIONI_AGGIUNTIVE_PRODUTTORE,
            RICHIESTA_INFORMAZIONI_AGGIUNTIVE_TRASFORMATORE;

    public RichiestaCollaborazioneRepository RICHIESTA_PRODUTTORE, RICHIESTA_TRASFORMATORE, RICHIESTA_DISTRIBUTORE,
            RICHIESTA_ANIMATORE, RICHIESTA_CURATORE;

    public RichiestaRepository RICHIESTA_TIPO_INFORMAZIONI_AGGIUNTIVE;

    public void pulisciRichieste(RichiestaRepository richiestaRepository) {
        richiestaRepository.deleteAll();
        richiestaRepository.flush();
        isRichiestaRepositorySet = false;
    }

    public void impostaRichieste(RichiestaRepository richiestaRepository) {
        pulisciRichieste(richiestaRepository);
    }

    public void pulisciRichiesteCollaborazione(RichiestaCollaborazioneRepository richiestaCollaborazioneRepository) {
        richiestaCollaborazioneRepository.deleteAll();
        richiestaCollaborazioneRepository.flush();
        isRichiestaCollaborazioneRepositorySet = false;
    }

    public void impostaRichiesteCollaborazione(RichiestaCollaborazioneRepository richiestaCollaborazioneRepository) {
        pulisciRichiesteCollaborazione(richiestaCollaborazioneRepository);

    }

    public void pulisciRichiesteInformazioniAggiuntive(
            RichiestaInformazioniAggiuntiveRepository richiestaInformazioniAggiuntiveRepository) {
        richiestaInformazioniAggiuntiveRepository.deleteAll();
        richiestaInformazioniAggiuntiveRepository.flush();
        isRichiestaInformazioniAggiuntiveRepositorySet = false;
    }

    public void impostaRichiesteInformazioniAggiuntive(
            RichiestaInformazioniAggiuntiveRepository richiestaInformazioniAggiuntiveRepository) {
        pulisciRichiesteInformazioniAggiuntive(richiestaInformazioniAggiuntiveRepository);
    }

    public void pulisciIndirizzi(IndirizzoRepository indirizzoRepository) {
        indirizzoRepository.deleteAll();
        indirizzoRepository.flush();
        isIndirizzoRepositorySet = false;
    }

    public void impostaIndirizzi(IndirizzoRepository indirizzoRepository) {
        pulisciIndirizzi(indirizzoRepository);
    }

    public void pulisciAziende(AziendaRepository aziendaRepository) {
        aziendaRepository.deleteAll();
        aziendaRepository.flush();
        isAziendaRepositorySet = false;
    }

    public void impostaAziende(AziendaRepository aziendaRepository) {
        pulisciAziende(aziendaRepository);
    }

    public void pulisciUtenti(UtenteRepository repo) {
        repo.deleteAll();
        repo.flush();
        isUtenteRepositorySet = false;
    }

    public void impostaUtenti(UtenteRepository repo) {
        pulisciUtenti(repo);
        List<Utente> utenti = new ArrayList<>(Arrays.asList(
                new Utente("Mario", "Rossi", "test@test.com", "password", "1234567890"),
                new Utente("Luca", "Bianchi", "test1@test.com", "password", "1234567890"),
                new Utente("Giovanni", "Verdi", "test2@test.com", "password", "1234567890"),
                new Utente("Paolo", "Neri", "test3@test.com", "password", "1234567890"),
                new Utente("Giuseppe", "Gialli", "test4@test.com", "password", "1234567890"),
                new Utente("Antonio", "Arancioni", "test5@test.com", "password", "1234567890"),
                new Utente("Francesco", "Blu", "test6@test.com", "password", "1234567890")));

        utenti.get(0).setRuolo(Ruolo.PRODUTTORE);
        utenti.get(1).setRuolo(Ruolo.TRASFORMATORE);
        utenti.get(2).setRuolo(Ruolo.DISTRIBUTORE);
        utenti.get(3).setRuolo(Ruolo.ANIMATORE);
        utenti.get(4).setRuolo(Ruolo.ACQUIRENTE);
        utenti.get(5).setRuolo(Ruolo.CURATORE);
        utenti.get(6).setRuolo(Ruolo.GESTORE);

        PRODUTTORE = repo.save(utenti.get(0));
        TRASFORMATORE = repo.save(utenti.get(1));
        DISTRIBUTORE = repo.save(utenti.get(2));
        ANIMATORE = repo.save(utenti.get(3));
        ACQUIRENTE = repo.save(utenti.get(4));
        CURATORE = repo.save(utenti.get(5));
        GESTORE = repo.save(utenti.get(6));

        repo.flush();
        isUtenteRepositorySet = true;
    }

}
