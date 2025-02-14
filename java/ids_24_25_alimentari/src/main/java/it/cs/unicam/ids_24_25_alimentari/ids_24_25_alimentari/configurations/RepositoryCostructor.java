package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.InformazioniAggiuntive;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.*;
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
    private InformazioniAggiuntiveRepository informazioniAggiuntiveRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @PostConstruct
    public void init() {
        impostaUtenti(utenteRepository);
        impostaIndirizzi(indirizzoRepository);
        impostaAziende(aziendaRepository);
        impostaRichiesteInformazioniAggiuntive(informazioniAggiuntiveRepository);
        impostaRichiesteCollaborazione(richiestaCollaborazioneRepository);
        impostaRichieste(richiestaRepository);
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

    public InformazioniAggiuntive RICHIESTA_INFORMAZIONI_AGGIUNTIVE_PRODUTTORE,
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
            InformazioniAggiuntiveRepository informazioniAggiuntiveRepository) {
        informazioniAggiuntiveRepository.deleteAll();
        informazioniAggiuntiveRepository.flush();
        isRichiestaInformazioniAggiuntiveRepositorySet = false;
    }

    public void impostaRichiesteInformazioniAggiuntive(
            InformazioniAggiuntiveRepository informazioniAggiuntiveRepository) {
        pulisciRichiesteInformazioniAggiuntive(informazioniAggiuntiveRepository);
    }

    public void pulisciIndirizzi(IndirizzoRepository indirizzoRepository) {
        indirizzoRepository.deleteAll();
        indirizzoRepository.flush();
        isIndirizzoRepositorySet = false;
    }

    public void pulisciAziende(AziendaRepository aziendaRepository) {
        aziendaRepository.deleteAll();
        aziendaRepository.flush();
        isAziendaRepositorySet = false;
    }
    public void pulisciUtenti(UtenteRepository repo) {
        repo.deleteAll();
        repo.flush();
        isUtenteRepositorySet = false;
    }

    public void impostaUtenti(UtenteRepository repo) {
        pulisciUtenti(repo);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("password");
        List<Utente> utenti = new ArrayList<>(Arrays.asList(
                new Utente("Mario", "Rossi", "test@test.com", password, "1234567890"),
                new Utente("Luca", "Bianchi", "test1@test.com", password, "1234567890"),
                new Utente("Giovanni", "Verdi", "test2@test.com", password, "1234567890"),
                new Utente("Paolo", "Neri", "test3@test.com", password, "1234567890"),
                new Utente("Giuseppe", "Gialli", "test4@test.com", password, "1234567890"),
                new Utente("Antonio", "Arancioni", "test5@test.com", password, "1234567890"),
                new Utente("Francesco", "Blu", "test6@test.com", password, "1234567890")));

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
    public void impostaIndirizzi(IndirizzoRepository indirizzoRepository) {
        pulisciIndirizzi(indirizzoRepository);

        // Creazione e salvataggio degli indirizzi
        INDIRIZZO_PRODUTTORE = new Indirizzo();
        INDIRIZZO_PRODUTTORE.setCitta("Milano");
        INDIRIZZO_PRODUTTORE.setCap("20100");
        INDIRIZZO_PRODUTTORE.setVia("Via Roma");
        INDIRIZZO_PRODUTTORE.setNumeroCivico("1");
        INDIRIZZO_PRODUTTORE.setProvincia("MI");
        INDIRIZZO_PRODUTTORE.setCoordinate("45.4642, 9.1900");

        INDIRIZZO_TRASFORMATORE = new Indirizzo();
        INDIRIZZO_TRASFORMATORE.setCitta("Torino");
        INDIRIZZO_TRASFORMATORE.setCap("10100");
        INDIRIZZO_TRASFORMATORE.setVia("Via Garibaldi");
        INDIRIZZO_TRASFORMATORE.setNumeroCivico("5");
        INDIRIZZO_TRASFORMATORE.setProvincia("TO");
        INDIRIZZO_TRASFORMATORE.setCoordinate("45.0703, 7.6869");

    }

    public void impostaAziende(AziendaRepository aziendaRepository) {
        pulisciAziende(aziendaRepository);

        AZIENDA_PRODUTTORE = new Azienda();
        AZIENDA_PRODUTTORE.setDenominazioneSociale("Azienda Agricola Rossi");
        AZIENDA_PRODUTTORE.setIva("IT12345678901");
        AZIENDA_PRODUTTORE.setIban("IT60X0542811101000000123456");
        AZIENDA_PRODUTTORE.setSedeLegale(INDIRIZZO_PRODUTTORE);
        AZIENDA_PRODUTTORE.setSedeOperativa(INDIRIZZO_PRODUTTORE);

        AZIENDA_TRASFORMATORE = new Azienda();
        AZIENDA_TRASFORMATORE.setDenominazioneSociale("Industria Alimentare Bianchi");
        AZIENDA_TRASFORMATORE.setIva("IT09876543210");
        AZIENDA_TRASFORMATORE.setIban("IT20Z0300203280000400167890");
        AZIENDA_TRASFORMATORE.setSedeLegale(INDIRIZZO_TRASFORMATORE);
        AZIENDA_TRASFORMATORE.setSedeOperativa(INDIRIZZO_TRASFORMATORE);

        AZIENDA_PRODUTTORE = aziendaRepository.save(AZIENDA_PRODUTTORE);
        AZIENDA_TRASFORMATORE = aziendaRepository.save(AZIENDA_TRASFORMATORE);

        aziendaRepository.flush();  // Assicura la persistenza immediata

        // Associare le aziende agli utenti
        PRODUTTORE.setIdAzienda(AZIENDA_PRODUTTORE.getId());
        TRASFORMATORE.setIdAzienda(AZIENDA_TRASFORMATORE.getId());

        utenteRepository.save(PRODUTTORE);
        utenteRepository.save(TRASFORMATORE);
        utenteRepository.flush();

        isAziendaRepositorySet = true;
    }

}
