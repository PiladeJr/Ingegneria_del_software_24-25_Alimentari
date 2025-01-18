package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.RichiestaCollaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.Ruolo;

import java.io.File;

public class BuilderRichiestaCollaborazione {
    private RichiestaCollaborazione collaborazione;

    public void costruisciNome(String nome) {
        collaborazione.setNome(nome);
    }

    public void costruisciCognome(String cognome) {
        collaborazione.setCognome(cognome);
    }

    public void costruisciTelefono(String telefono) {
        collaborazione.setCognome(telefono);
    }

    public void costruisciEmail(String email) {
        collaborazione.setEmail(email);
    }

    public void costruisciRuolo(Ruolo ruolo) {
        collaborazione.setRuolo(ruolo);
    }

    public void costruisciDenSociale(String denSociale) {
        collaborazione.setDenominazioneSociale(denSociale);
    }

    public void costruisciSedeLegale(String sedeLegale) {
        collaborazione.setSedeLegale(sedeLegale);
    }

    public void costruisciSedeOperativa(String sedeOperativa) {
        collaborazione.setSedeOperativa(sedeOperativa);
    }

    public void costruisciIban(String iban) {
        collaborazione.setIban(iban);
    }

    public void costruisciIva(String iva) {
        collaborazione.setIva(iva);
    }

    public void aggiungiCv(File cv) {
        collaborazione.setCv(cv);
    }

    public void aggiungiCartaIdentita(File cartaIdentita) {
        collaborazione.setCartaIdentita(cartaIdentita);
    }

    public void aggiungiCertificato(File certificato) {
        collaborazione.setCertificato(certificato);
    }

    public void costruisciReferente(String referente) {
        collaborazione.setAziendaReferente(referente);
    }

    public RichiestaCollaborazione getRichiesta() {
        return collaborazione;
    }

}
