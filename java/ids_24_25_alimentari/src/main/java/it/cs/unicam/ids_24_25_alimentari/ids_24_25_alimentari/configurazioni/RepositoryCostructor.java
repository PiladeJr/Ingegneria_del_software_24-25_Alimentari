package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.configurazioni;

import java.io.File;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.InformazioniAggiuntiveBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.InformazioniAggiuntive;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.ProdottoSingolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoFiera;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoVisita;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.StatusEvento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.TipologiaEvento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.*;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.AziendaService;
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
    private EventoRepository eventoRepository;

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
        impostaEventi(eventoRepository);
    }

    public boolean isIndirizzoRepositorySet = false;
    public boolean isRichiestaRepositorySet = false;
    public boolean isInformazioniAggiuntiveRepositorySet = false;
    public boolean isProdottoSingoloRepositorySet = false;
    public boolean isRichiestaCollaborazioneRepositorySet = false;
    public boolean isUtenteRepositorySet = false;
    public boolean isAziendaRepositorySet = false;
    public boolean isUtenteAziendaEsternaRepositorySet = false;
    public boolean isEventoRepositorySet = false;
//    public boolean isFieraRepositorySet = false;
//    public boolean isVisitaRepositorySet = false;

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
            INDIRIZZO_DISTRIBUTORE,
            INDIRIZZO_FIERA_AUTUNNO,
            INDIRIZZO_FIERA_CONTADINA;

    public InformazioniAggiuntive
            INFORMAZIONI_AGGIUNTIVE_PRODUTTORE,
            INFORMAZIONI_AGGIUNTIVE_TRASFORMATORE;

    public ProdottoSingolo
            PRODOTTO_LATTE,
            PRODOTTO_BURRO,
            PRODOTTO_FORMAGGIO;

    public EventoFiera
            FIERA_CONTADINA,
            FIERA_AUTUNNO;

    public EventoVisita
            VISITA_PRODUTTORE,
            VISITA_TRASFORMATORE;

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
    public void pulisciEventi(EventoRepository repo){
        repo.deleteAll();
        repo.flush();
        isEventoRepositorySet = false;
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

        INDIRIZZO_FIERA_AUTUNNO = new Indirizzo();
        INDIRIZZO_FIERA_AUTUNNO.setCitta("Vercelli");
        INDIRIZZO_FIERA_AUTUNNO.setCap("13100");
        INDIRIZZO_FIERA_AUTUNNO.setVia("Corso Italia");
        INDIRIZZO_FIERA_AUTUNNO.setNumeroCivico("10");
        INDIRIZZO_FIERA_AUTUNNO.setProvincia("VC");
        INDIRIZZO_FIERA_AUTUNNO.setCoordinate("45.3203, 8.4222");

        INDIRIZZO_FIERA_CONTADINA = new Indirizzo();
        INDIRIZZO_FIERA_CONTADINA.setCitta("Treviglio");
        INDIRIZZO_FIERA_CONTADINA.setCap("24047");
        INDIRIZZO_FIERA_CONTADINA.setVia("Piazza Garibaldi");
        INDIRIZZO_FIERA_CONTADINA.setNumeroCivico("2");
        INDIRIZZO_FIERA_CONTADINA.setProvincia("BG");
        INDIRIZZO_FIERA_CONTADINA.setCoordinate("45.6042, 9.5911");

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

    public void impostaEventi(EventoRepository repo){
        try {
            File immagine1 = new File(getClass().getClassLoader().getResource("facSimileLocandinaFiera2.jpeg").toURI());
            File immagine2 = new File(getClass().getClassLoader().getResource("facSimileLocandinaFiera.jpeg").toURI());
            File immagine3 = new File(getClass().getClassLoader().getResource("visita.jpeg").toURI());
            File immagine4 = new File(getClass().getClassLoader().getResource("visita2.jpeg").toURI());

            pulisciEventi(repo);
            FIERA_CONTADINA = new EventoFiera();
            FIERA_CONTADINA.setTitolo("Fiera Contadina");
            FIERA_CONTADINA.setDescrizione("Fiera dedicata ai prodotti locali e biologici");
            FIERA_CONTADINA.setTipologia(TipologiaEvento.FIERA);
            FIERA_CONTADINA.setStatus(StatusEvento.PROGRAMMATO);
            FIERA_CONTADINA.setInizio(LocalDateTime.of(2025, 4, 28, 10, 0));
            FIERA_CONTADINA.setFine(LocalDateTime.of(2025, 5, 1, 18, 0));
            FIERA_CONTADINA.setLocandina(immagine1);
            FIERA_CONTADINA.setIndirizzo(INDIRIZZO_FIERA_CONTADINA);
            FIERA_CONTADINA.setCreatore(ANIMATORE);
            FIERA_CONTADINA.setAziendePresenti(new ArrayList<>(Arrays.asList(AZIENDA_PRODUTTORE, AZIENDA_TRASFORMATORE)));

            FIERA_AUTUNNO = new EventoFiera();
            FIERA_AUTUNNO.setTitolo("Fiera di Autunno");
            FIERA_AUTUNNO.setDescrizione("Fiera dedicata ai prodotti autunnali e locali");
            FIERA_AUTUNNO.setTipologia(TipologiaEvento.FIERA);
            FIERA_AUTUNNO.setStatus(StatusEvento.PROGRAMMATO);
            FIERA_AUTUNNO.setInizio(LocalDateTime.of(2025, 10, 9, 10, 0));
            FIERA_AUTUNNO.setFine(LocalDateTime.of(2025, 10, 9, 18, 0));
            FIERA_AUTUNNO.setLocandina(immagine2);
            FIERA_AUTUNNO.setIndirizzo(INDIRIZZO_FIERA_AUTUNNO);
            FIERA_AUTUNNO.setCreatore(ANIMATORE);
            FIERA_AUTUNNO.setAziendePresenti(new ArrayList<>(Arrays.asList(AZIENDA_PRODUTTORE, AZIENDA_TRASFORMATORE)));

            VISITA_PRODUTTORE = new EventoVisita();
            VISITA_PRODUTTORE.setTitolo("Visita Aziendale");
            VISITA_PRODUTTORE.setDescrizione("Visita guidata presso l'azienda agricola");
            VISITA_PRODUTTORE.setTipologia(TipologiaEvento.VISITA);
            VISITA_PRODUTTORE.setStatus(StatusEvento.PROGRAMMATO);
            VISITA_PRODUTTORE.setInizio(LocalDateTime.of(2025, 6, 15, 10, 0));
            VISITA_PRODUTTORE.setFine(LocalDateTime.of(2025, 6, 15, 18, 0));
            VISITA_PRODUTTORE.setLocandina(immagine3);
            //VISITA_PRODUTTORE.setIndirizzo(indirizzoRepository.findById(INDIRIZZO_PRODUTTORE.getId()).orElseThrow());
            //VISITA_PRODUTTORE.setAziendaRiferimento(aziendaRepository.findById(AZIENDA_PRODUTTORE.getId()).orElseThrow());
            VISITA_PRODUTTORE.setCreatore(ANIMATORE);
            VISITA_PRODUTTORE.setIscritti(new ArrayList<>());

            VISITA_TRASFORMATORE = new EventoVisita();
            VISITA_TRASFORMATORE.setTitolo("Visita Aziendale Trasformatore");
            VISITA_TRASFORMATORE.setDescrizione("Visita guidata presso l'azienda di trasformazione");
            VISITA_TRASFORMATORE.setTipologia(TipologiaEvento.VISITA);
            VISITA_TRASFORMATORE.setStatus(StatusEvento.PROGRAMMATO);
            VISITA_TRASFORMATORE.setInizio(LocalDateTime.of(2025, 7, 20, 10, 0));
            VISITA_TRASFORMATORE.setFine(LocalDateTime.of(2025, 7, 20, 18, 0));
            VISITA_TRASFORMATORE.setLocandina(immagine4);
            //VISITA_TRASFORMATORE.setIndirizzo(indirizzoRepository.findById(INDIRIZZO_TRASFORMATORE.getId()).orElseThrow());
            //VISITA_TRASFORMATORE.setAziendaRiferimento(aziendaRepository.findById(AZIENDA_TRASFORMATORE.getId()).orElseThrow());
            VISITA_TRASFORMATORE.setCreatore(ANIMATORE);
            VISITA_TRASFORMATORE.setIscritti(new ArrayList<>());

            repo.save(VISITA_PRODUTTORE);
            repo.save(VISITA_TRASFORMATORE);
            repo.save(FIERA_CONTADINA);
            repo.save(FIERA_AUTUNNO);

            isEventoRepositorySet = true;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
