package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.configurations;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders.InformazioniAggiuntiveBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.contenuto.InformazioniAggiuntive;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.contenuto.prodotto.ProdottoSingolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.UtenteAziendaEsterna;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.*;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.AziendaService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.InfoAggiuntiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
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
    private InformazioniAggiuntiveRepository informazioniAggiuntiveRepository;

    @Autowired
    private RichiestaCollaborazioneRepository richiestaCollaborazioneRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private UtenteAziendaEsternaRepository utenteAziendaEsternaRepository;

    @Autowired
    private ProdottoSingoloRepository prodottoSingoloRepository;

    @Autowired
    private AziendaService aziendaService;

    @PostConstruct
    public void init() {
        impostaUtenti(utenteRepository);
        impostaIndirizzi(indirizzoRepository);
        impostaAziende(aziendaRepository);
        impostaInfoAggiuntive(informazioniAggiuntiveRepository);
        impostaProdottiSingoli(prodottoSingoloRepository);
        impostaRichiesteCollaborazione(richiestaCollaborazioneRepository);
        impostaRichieste(richiestaRepository);
        impostaAziendeEsterne(utenteAziendaEsternaRepository);
    }

    public boolean isIndirizzoRepositorySet = false;
    public boolean isRichiestaRepositorySet = false;
    public boolean isInformazioniAggiuntiveRepositorySet = false;
    public boolean isProdottoSingoloRepositorySet = false;
    public boolean isRichiestaCollaborazioneRepositorySet = false;
    public boolean isUtenteRepositorySet = false;
    public boolean isAziendaRepositorySet = false;
    public boolean isUtenteAziendaEsternaRepositorySet = false;

    public Utente
            PRODUTTORE,
            TRASFORMATORE,
            DISTRIBUTORE,
            ANIMATORE,
            ACQUIRENTE,
            CURATORE,
            GESTORE;

    public Azienda
            AZIENDA_PRODUTTORE,
            AZIENDA_TRASFORMATORE,
            AZIENDA_DISTRIBUTORE;

    public Indirizzo
            INDIRIZZO_PRODUTTORE,
            INDIRIZZO_TRASFORMATORE,
            INDIRIZZO_DISTRIBUTORE;

    public InformazioniAggiuntive
            INFORMAZIONI_AGGIUNTIVE_PRODUTTORE,
            INFORMAZIONI_AGGIUNTIVE_TRASFORMATORE;

    public ProdottoSingolo
            PRODOTTO_LATTE,
            PRODOTTO_BURRO,
            PRODOTTO_FORMAGGIO;

    public RichiestaCollaborazioneRepository RICHIESTA_PRODUTTORE, RICHIESTA_TRASFORMATORE, RICHIESTA_DISTRIBUTORE,
            RICHIESTA_ANIMATORE, RICHIESTA_CURATORE;

    public RichiestaRepository RICHIESTA_TIPO_INFORMAZIONI_AGGIUNTIVE;
    public UtenteAziendaEsternaRepository UTENTE_TRASFORMATORE_PRODUTTORE;

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

    public void pulisciAziendeEsterne(UtenteAziendaEsternaRepository utenteAziendaEsternaRepository) {
        utenteAziendaEsternaRepository.deleteAll();
        utenteAziendaEsternaRepository.flush();
        isUtenteAziendaEsternaRepositorySet = false;
    }
    public void impostaAziendeEsterne(UtenteAziendaEsternaRepository utenteAziendaEsternaRepository) {
        pulisciAziendeEsterne(utenteAziendaEsternaRepository);
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

    public void pulisciInfoAggiuntive(InformazioniAggiuntiveRepository repo){
        repo.deleteAll();
        repo.flush();
        isInformazioniAggiuntiveRepositorySet = false;
    }

    public void pulisciProdottiSingoli(ProdottoSingoloRepository repo){
        repo.deleteAll();
        repo.flush();
        isProdottoSingoloRepositorySet = false;
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

    public void impostaInfoAggiuntive(InformazioniAggiuntiveRepository repo){
        try {
            File immagine = new File(getClass().getClassLoader().getResource("azienda1.jpg").toURI());

            File certificato = new File(getClass().getClassLoader().getResource("certificato.pdf").toURI());

            InformazioniAggiuntiveBuilder builder = new InformazioniAggiuntiveBuilder();

            builder.costruisciDescrizione("La nostra azienda vi garantisce il miglior prodotto nostrano della nostra terra");
            builder.costruisciProduzione("Frutta e Verdura");
            builder.costruisciMetodi("Metodi sostenibili, utilizzando solo energia rinnovabile");
            builder.aggiungiImmagine(immagine);
            builder.aggiungiCertificato(certificato);
            INFORMAZIONI_AGGIUNTIVE_PRODUTTORE = builder.getInformazioniAggiuntive();

            builder.costruisciDescrizione("Dalle migliori materie prime per la qualita' che meritate");
            builder.costruisciProduzione("Tecniche avanzate e tecnologia industriale all'avanguardia");
            builder.costruisciMetodi("Metodi sostenibili e nessuno spreco di risorse");
            builder.aggiungiImmagine(immagine);
            builder.aggiungiCertificato(certificato);
            INFORMAZIONI_AGGIUNTIVE_TRASFORMATORE = builder.getInformazioniAggiuntive();

            AZIENDA_PRODUTTORE.setInformazioniAggiuntive(INFORMAZIONI_AGGIUNTIVE_PRODUTTORE);
            AZIENDA_TRASFORMATORE.setInformazioniAggiuntive(INFORMAZIONI_AGGIUNTIVE_TRASFORMATORE);

            repo.save(INFORMAZIONI_AGGIUNTIVE_PRODUTTORE);
            repo.save(INFORMAZIONI_AGGIUNTIVE_TRASFORMATORE);

            aziendaRepository.save(AZIENDA_PRODUTTORE);
            aziendaRepository.save(AZIENDA_TRASFORMATORE);

            aziendaService.CollegaAzienda(TRASFORMATORE.getId(), AZIENDA_PRODUTTORE.getId());

            isInformazioniAggiuntiveRepositorySet = true;

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void impostaProdottiSingoli(ProdottoSingoloRepository repo){
        try {
            List<File> immaginiProdotto = new ArrayList<>(Arrays.asList(
                    new File(getClass().getClassLoader().getResource("latte.jpg").toURI()),
                    new File(getClass().getClassLoader().getResource("latte1.jpeg").toURI())
            ));

            List<File> immaginiProdotto1 = new ArrayList<>(Arrays.asList(
                    new File(getClass().getClassLoader().getResource("burro.jpeg").toURI()),
                    new File(getClass().getClassLoader().getResource("burro1.jpg").toURI())
            ));

            List<File> immaginiProdotto2 = new ArrayList<>(Arrays.asList(
                    new File(getClass().getClassLoader().getResource("formaggio.jpg").toURI()),
                    new File(getClass().getClassLoader().getResource("formaggio1.jpeg").toURI())
            ));


            List<ProdottoSingolo> prodSing = new ArrayList<>(Arrays.asList(
                    new ProdottoSingolo("Latte Rossi", "Latte scremato ad alta digeribilita'", 4.99, 1L, immaginiProdotto, 50, "latticini", "allevamento bestiame sostenibile"),
                    new ProdottoSingolo("Burro Bianchi", "Burro di altissima qualita'", 2.99, 2L, immaginiProdotto1, 25, "latticini", "Latte selezionato e senza conservanti"),
                    new ProdottoSingolo("Formaggio Bianchi", "Formaggio fresco dal sapore delicato e fresco", 3.99, 2L, immaginiProdotto2, 15, "latticini", "Latte selezionato e senza conservanti, con tecniche sostenibili")
            ));


            PRODOTTO_LATTE = repo.save(prodSing.get(0));
            PRODOTTO_BURRO = repo.save(prodSing.get(1));
            PRODOTTO_FORMAGGIO = repo.save(prodSing.get(2));

            isProdottoSingoloRepositorySet = true;

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
