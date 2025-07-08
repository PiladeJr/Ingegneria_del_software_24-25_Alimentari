package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;

import java.io.File;

/**
 * -- GETTER --
 * design pattern BUILDER che restituisce l'oggetto azienda creato
 *
 * @return l'oggetto azienda creato
 */

public class AziendaBuilder {
    Azienda azienda;

    public AziendaBuilder() {
        reset();
    }

    public void costruisciDenSociale(String denSociale) {
        this.azienda.setDenominazioneSociale(denSociale);
    }

    public void costruisciSedeLegale(Indirizzo sedeLegale) {
        this.azienda.setSedeLegale(sedeLegale);
    }

    public void costruisciSedeOperativa(Indirizzo sedeOperativa) {
        this.azienda.setSedeOperativa(sedeOperativa);
    }

    public void costruisciIva(String iva) {
        this.azienda.setIva(iva);
    }

    public void aggiungiCertificato(File certificato) {
        this.azienda.setCertificato(certificato);
    }

    public void costruisciUtente(Utente utente) {
        this.azienda.setUtente(utente);
    }

    public Azienda getAzienda() {
        Azienda a = this.azienda;
        this.reset();
        return a;
    }

    public void reset() {
        this.azienda = new Azienda();
    }

}
