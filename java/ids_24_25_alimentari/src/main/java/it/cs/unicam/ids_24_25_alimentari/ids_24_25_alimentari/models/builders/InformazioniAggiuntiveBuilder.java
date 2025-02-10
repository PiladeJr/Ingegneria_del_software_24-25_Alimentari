package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.InformazioniAggiuntive;

import java.io.File;

public class InformazioniAggiuntiveBuilder {
    private InformazioniAggiuntive informazioniAggiuntive;
    public InformazioniAggiuntiveBuilder() {
        reset();
    }
    public InformazioniAggiuntiveBuilder costruisciDescrizione(String descrizioneAzienda) {
        this.informazioniAggiuntive.setDescrizioneAzienda(descrizioneAzienda);
        return this;
    }
    public InformazioniAggiuntiveBuilder costruisciProduzione(String descrizioneProduzione){
        this.informazioniAggiuntive.setDescrizioneProduzione(descrizioneProduzione);
        return this;
    }
    public InformazioniAggiuntiveBuilder costruisciMetodi(String descrizioneMetodi){
        this.informazioniAggiuntive.setDescrizioneMetodi(descrizioneMetodi);
        return this;
    }
    public InformazioniAggiuntiveBuilder aggiungiImmagine(File immagine){
        this.informazioniAggiuntive.getImmagini().add(immagine);
        return this;
    }
    public InformazioniAggiuntiveBuilder aggiungiCertificato(File certificato){
        this.informazioniAggiuntive.getCertificati().add(certificato);
        return this;
    }
    public InformazioniAggiuntiveBuilder rimuoviImmagine(File immagine){
        this.informazioniAggiuntive.getImmagini().remove(immagine);
        return this;
    }
    public InformazioniAggiuntiveBuilder rimuoviCertificato(File certificato){
        this.informazioniAggiuntive.getCertificati().remove(certificato);
        return this;
    }
    public InformazioniAggiuntive getInformazioniAggiuntive() {
        InformazioniAggiuntive i = this.informazioniAggiuntive;
        reset();
        return i;
    }

    public void reset(){
        this.informazioniAggiuntive = new InformazioniAggiuntive();
    }

}
