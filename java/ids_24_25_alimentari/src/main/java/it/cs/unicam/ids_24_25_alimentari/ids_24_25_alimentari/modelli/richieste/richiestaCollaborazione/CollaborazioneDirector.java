package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione;

import java.io.File;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.ContenutoBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;

public class CollaborazioneDirector {
    private ContenutoBuilder builder;

    public CollaborazioneDirector() {
        this.builder = new ContenutoBuilder();
    }

    private void costruisciAccount(
            String nome,
            String cognome,
            String telefono,
            String email,
            Ruolo ruolo,
            String iban,
            File cartaIdentita
    ){
        builder.costruisciNome(nome);
        builder.costruisciCognome(cognome);
        builder.costruisciTelefono(telefono);
        builder.costruisciEmail(email);
        builder.costruisciRuolo(ruolo);
        builder.costruisciIban(iban);
        builder.costruisciCartaIdentita(cartaIdentita);

    }

    private void costruisciAzienda(
            String denSociale,
            Indirizzo sedeLegale,
            Indirizzo sedeOperativa,
            String iva,
            File certificato
    ) {
        builder.costruisciDenSociale(denSociale);
        builder.costruisciSedeLegale(sedeLegale);
        builder.costruisciSedeOperativa(sedeOperativa);
        builder.costruisciIva(iva);
        builder.aggiungiCertificato(certificato);
    }

    /**
     * Crea un'azienda costruendo i relativi dati attraverso un builder.
     * per impostare i dati relativi all'azienda,
     * le informazioni personali del rappresentante, i dettagli aziendali,
     * le sedi, le coordinate bancarie e i documenti richiesti.
     *
     * @param nome           Il nome del rappresentante dell'azienda.
     * @param cognome        Il cognome del rappresentante dell'azienda.
     * @param telefono       Il numero di telefono del rappresentante.
     * @param email          L'indirizzo email del rappresentante.
     * @param ruolo          Il ruolo dell'azienda selezionato tra PRODUTTORE, TRASFORMATORE E DISTRIBUTORE.
     * @param denSociale     La denominazione sociale dell'azienda.
     * @param sedeLegale     L'indirizzo della sede legale dell'azienda.
     * @param sedeOperativa  L'indirizzo della sede operativa dell'azienda.
     * @param iban           Il codice IBAN dell'azienda per le transazioni bancarie.
     * @param iva            Il numero di partita IVA dell'azienda.
     * @param certificato    Il file contenente il certificato aziendale richiesto.
     * @param cartaIdentita  Il file contenente la carta d'identità del rappresentante.
     */
    public Collaborazione creaAzienda(
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
        costruisciAccount(nome, cognome, telefono, email, ruolo, iban, cartaIdentita);
        costruisciAzienda(denSociale, sedeLegale, sedeOperativa, iva, certificato);
        return builder.build();
    }

    /**
     * Crea un animatore costruendo i relativi dati attraverso un builder
     * le informazioni personali, i dettagli bancari e l'azienda di riferimento
     * se presente.
     *
     * @param nome            Il nome dell'animatore.
     * @param cognome         Il cognome dell'animatore.
     * @param telefono        Il numero di telefono dell'animatore.
     * @param email           L'indirizzo email dell'animatore.
     * @param ruolo           indica che si tratta di una richiesta di tipo animatore.
    // * @param aziendaReferente Il nome dell'azienda referente per l'animatore.
     * @param iban            Il codice IBAN dell'animatore per le transazioni bancarie.
     * @param cartaIdentita   Il file contenente la carta d'identità dell'animatore.
     */
    public Collaborazione creaAnimatore(
            String nome,
            String cognome,
            String telefono,
            String email,
            Ruolo ruolo,
            String iban,
            File cartaIdentita) {
        costruisciAccount(nome, cognome, telefono, email, ruolo, iban, cartaIdentita);
        return builder.build();

    }

    /**
     * Crea un curatore costruendo i relativi dati attraverso un builder.
     * per impostare le informazioni personali, i dettagli bancari e i documenti richiesti.
     *
     * @param nome          Il nome del curatore.
     * @param cognome       Il cognome del curatore.
     * @param telefono      Il numero di telefono del curatore.
     * @param email         L'indirizzo email del curatore.
     * @param ruolo         indica che si tratta di una richiesta di tipo Curatore.
     * @param iban          Il codice IBAN del curatore per le transazioni bancarie.
     * @param cartaIdentita Il file contenente la carta d'identità del curatore.
     * @param cv            Il file contenente il curriculum vitae del curatore.
     */
    public Collaborazione creaCuratore(
            String nome,
            String cognome,
            String telefono,
            String email,
            Ruolo ruolo,
            String iban,
            File cartaIdentita,
            File cv

    ) {
        costruisciAccount(nome, cognome, telefono, email, ruolo, iban, cartaIdentita);
        builder.costruisciCV(cv);
        return builder.build();
    }

}