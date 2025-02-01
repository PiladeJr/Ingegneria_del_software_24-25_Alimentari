package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models;

import java.io.File;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders.BuilderRichiestaCollaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;

public class RichiestaCollaborazioneDirector {
    private BuilderRichiestaCollaborazione builder;

    public RichiestaCollaborazioneDirector(BuilderRichiestaCollaborazione builder) {
        this.builder = builder;
    }

    public void creaAzienda(
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
        builder.costruisciNome(nome);
        builder.costruisciCognome(cognome);
        builder.costruisciTelefono(telefono);
        builder.costruisciEmail(email);
        builder.costruisciRuolo(ruolo);
        builder.costruisciDenSociale(denSociale);
        builder.costruisciSedeLegale(sedeLegale);
        builder.costruisciSedeOperativa(sedeOperativa);
        builder.costruisciIban(iban);
        builder.costruisciIva(iva);
        builder.aggiungiCertificato(certificato);
        builder.aggiungiCartaIdentita(cartaIdentita);
    }

    public void creaAnimatore(
            String nome,
            String cognome,
            String telefono,
            String email,
            Ruolo ruolo,
            String aziendaReferente,
            String iban,
            File cartaIdentita) {
        builder.costruisciNome(nome);
        builder.costruisciCognome(cognome);
        builder.costruisciTelefono(telefono);
        builder.costruisciEmail(email);
        builder.costruisciRuolo(ruolo);
        builder.costruisciReferente(aziendaReferente);
        builder.costruisciIban(iban);
        builder.aggiungiCartaIdentita(cartaIdentita);
    }

    public void creaCuratore(
            String nome,
            String cognome,
            String telefono,
            String email,
            Ruolo ruolo,
            String iban,
            File cartaIdentita,
            File cv

    ) {
        builder.costruisciNome(nome);
        builder.costruisciCognome(cognome);
        builder.costruisciTelefono(telefono);
        builder.costruisciEmail(email);
        builder.costruisciRuolo(ruolo);
        builder.costruisciIban(iban);
        builder.aggiungiCartaIdentita(cartaIdentita);
        builder.aggiungiCv(cv);
    }

}