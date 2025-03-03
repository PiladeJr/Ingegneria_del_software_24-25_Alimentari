package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.azienda.Indirizzo;

import java.io.File;
import java.util.List;

/**
 * -- GETTER --
 *  metodo che restituisce l'oggetto utente creato
 *
 * @return l'oggetto utente creato
 */

public class AziendaBuilder {
    Azienda azienda;

    public AziendaBuilder() {
        reset();
    }

    public void costruisciDenSociale(String denSociale){ this.azienda.setDenominazioneSociale(denSociale); }

    public void costruisciSedeLegale(Indirizzo sedeLegale){ this.azienda.setSedeLegale(sedeLegale);}

    public void costruisciSedeOperativa(Indirizzo sedeOperativa){ this.azienda.setSedeOperativa(sedeOperativa);}

    public void costruisciIva(String iva){ this.azienda.setIva(iva); }

    public void costruisciIban(String iban){ this.azienda.setIban(iban); }

    public void aggiungiCertificato(File certificato){ this.azienda.setCertificato(certificato); }

    public Azienda getAzienda() {
        Azienda a = this.azienda;
        this.reset();
        return a;
    }

    public void reset(){
        this.azienda = new Azienda();
    }

}
