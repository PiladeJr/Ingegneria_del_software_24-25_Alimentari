package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info.InfoAzienda;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * -- GETTER --
 *  metodo che restituisce l'oggetto utente creato
 *
 * @return l'oggetto InformazioniAggiuntive creato
 */
public class InfoAziendaBuilder {
    private InfoAzienda infoAzienda;

    public InfoAziendaBuilder() {
        reset();
    }

    public void costruisciDescrizione(String descrizioneAzienda) {
        this.infoAzienda.setDescrizioneAzienda(descrizioneAzienda);
    }

    public void costruisciProduzione(String descrizioneProduzione) {
        this.infoAzienda.setDescrizioneProduzione(descrizioneProduzione);
    }

    public void costruisciMetodi(String descrizioneMetodi) {
        this.infoAzienda.setDescrizioneMetodi(descrizioneMetodi);
    }

    public void aggiungiImmagine(File immagine) {
        if (this.infoAzienda.getImmagini() == null) {
            List<File> immagini = new ArrayList<>();
            immagini.add(immagine);
            this.infoAzienda.setImmagini(immagini);
        } else {
            this.infoAzienda.getImmagini().add(immagine);
        }
    }

    public void aggiungiCertificato(File certificato) {
        if (this.infoAzienda.getCertificati() == null) {
            List<File> certificati = new ArrayList<>();
            certificati.add(certificato);
            this.infoAzienda.setCertificati(certificati);
        } else {
            this.infoAzienda.getCertificati().add(certificato);
        }
    }

    public void costruisciAzienda(Azienda azienda){
        this.infoAzienda.setAzienda(azienda);
    }

    public InfoAzienda build() {
        InfoAzienda i = this.infoAzienda;
        reset();
        return i;
    }

    public void reset() {
        this.infoAzienda = new InfoAzienda();
    }

}
