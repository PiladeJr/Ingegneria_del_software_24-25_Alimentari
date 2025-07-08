package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.InformazioniAggiuntive;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * -- GETTER --
 *  metodo che restituisce l'oggetto utente creato
 *
 * @return l'oggetto InformazioniAggiuntive creato
 */
public class InformazioniAggiuntiveBuilder {
    private InformazioniAggiuntive informazioniAggiuntive;

    public InformazioniAggiuntiveBuilder() {
        reset();
    }

    public void costruisciDescrizione(String descrizioneAzienda) {
        this.informazioniAggiuntive.setDescrizioneAzienda(descrizioneAzienda);
    }

    public void costruisciProduzione(String descrizioneProduzione) {
        this.informazioniAggiuntive.setDescrizioneProduzione(descrizioneProduzione);
    }

    public void costruisciMetodi(String descrizioneMetodi) {
        this.informazioniAggiuntive.setDescrizioneMetodi(descrizioneMetodi);
    }

    public void aggiungiImmagine(File immagine) {
        if (this.informazioniAggiuntive.getImmagini() == null) {
            List<File> immagini = new ArrayList<>();
            immagini.add(immagine);
            this.informazioniAggiuntive.setImmagini(immagini);
        } else {
            this.informazioniAggiuntive.getImmagini().add(immagine);
        }
    }

    public void aggiungiCertificato(File certificato) {
        if (this.informazioniAggiuntive.getCertificati() == null) {
            List<File> certificati = new ArrayList<>();
            certificati.add(certificato);
            this.informazioniAggiuntive.setCertificati(certificati);
        } else {
            this.informazioniAggiuntive.getCertificati().add(certificato);
        }
    }

    public void rimuoviImmagine(File immagine) {
        this.informazioniAggiuntive.getImmagini().remove(immagine);
    }

    public void rimuoviCertificato(File certificato) {
        this.informazioniAggiuntive.getCertificati().remove(certificato);
    }

    public void costruisciAzienda(Azienda azienda){
        this.informazioniAggiuntive.setAzienda(azienda);
    }

    public InformazioniAggiuntive getInformazioniAggiuntive() {
        InformazioniAggiuntive i = this.informazioniAggiuntive;
        reset();
        return i;
    }

    public void reset() {
        this.informazioniAggiuntive = new InformazioniAggiuntive();
    }

}
