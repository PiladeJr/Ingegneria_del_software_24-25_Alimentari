package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.InformazioniAggiuntive;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.*;
import lombok.Getter;

@Component
@ComponentScan(basePackageClasses = { InformazioniAggiuntiveRepository.class, IndirizzoRepository.class,
        RichiestaRepository.class, RichiestaCollaborazioneRepository.class, UtenteRepository.class,
        AziendaRepository.class })

public class RepositoryProvider {
    @Getter
    private final InformazioniAggiuntiveRepository informazioniAggiuntiveRepository;

    @Getter
    private final IndirizzoRepository indirizzoRepository;

    @Getter
    private final RichiestaRepository richiestaRepository;

    @Getter
    private final RichiestaCollaborazioneRepository richiestaCollaborazioneRepository;

    @Getter
    private final UtenteRepository utenteRepository;

    @Getter
    private final AziendaRepository aziendaRepository;

    @Autowired
    public RepositoryProvider(InformazioniAggiuntiveRepository informazioniAggiuntiveRepository,
            IndirizzoRepository indirizzoRepository, RichiestaRepository richiestaRepository,
            RichiestaCollaborazioneRepository richiestaCollaborazioneRepository, UtenteRepository utenteRepository,
            AziendaRepository aziendaRepository) {
        this.informazioniAggiuntiveRepository = informazioniAggiuntiveRepository;
        this.indirizzoRepository = indirizzoRepository;
        this.richiestaRepository = richiestaRepository;
        this.richiestaCollaborazioneRepository = richiestaCollaborazioneRepository;
        this.utenteRepository = utenteRepository;
        this.aziendaRepository = aziendaRepository;
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

    public void pulisciRichiesteInformazioniAggiuntive() {
        informazioniAggiuntiveRepository.deleteAll();
        informazioniAggiuntiveRepository.flush();
        isRichiestaInformazioniAggiuntiveRepositorySet = false;
    }

    public void impostaRichiesteInformazioniAggiuntive() {
        pulisciRichiesteInformazioniAggiuntive();
    }

    public void pulisciIndirizzi() {
        indirizzoRepository.deleteAll();
        indirizzoRepository.flush();
        isIndirizzoRepositorySet = false;
    }

    public void impostaIndirizzi() {
        pulisciIndirizzi();
    }

    public void pulisciAziende() {
        aziendaRepository.deleteAll();
        aziendaRepository.flush();
        isAziendaRepositorySet = false;
    }

    public void impostaAziende() {
        pulisciAziende();
    }

    public void pulisciUtenti() {
        utenteRepository.deleteAll();
        utenteRepository.flush();
        isUtenteRepositorySet = false;
    }

    public void impostaUtenti() {
        pulisciUtenti();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("password");
        List<Utente> utenti = new ArrayList<>(Arrays.asList(
                new Utente("Mario", "Rossi", "test@test.com",
                        password, "1234567890"),
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

        PRODUTTORE = utenteRepository.save(utenti.get(0));
        TRASFORMATORE = utenteRepository.save(utenti.get(1));
        DISTRIBUTORE = utenteRepository.save(utenti.get(2));
        ANIMATORE = utenteRepository.save(utenti.get(3));
        ACQUIRENTE = utenteRepository.save(utenti.get(4));
        CURATORE = utenteRepository.save(utenti.get(5));
        GESTORE = utenteRepository.save(utenti.get(6));

        utenteRepository.flush();

        isUtenteRepositorySet = true;

    }

}
