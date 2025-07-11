package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.Eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.EventoFiera;


import java.util.ArrayList;
import java.util.List;

public class FieraBuilder extends EventoBuilder<EventoFiera, FieraBuilder> {

    public FieraBuilder() {
        super();
    }

    /**
     * implementazione del metodo nuovaIstanza in EventoBuilder
     * @return una nuova istanza EventoFiera
     */
    @Override
    protected EventoFiera nuovaIstanza() {
        return new EventoFiera();
    }
    public FieraBuilder costruisciAziende(List<Azienda> aziende) {
        if (aziende == null) {
            evento.setAziendePresenti(new ArrayList<>());
        }
        else {
            evento.setAziendePresenti(new ArrayList<>(aziende));
        }

        return this;
    }
    //todo testare per vedere se funziona
    public FieraBuilder aggiungiAzienda(Azienda azienda) {
        evento.getAziendePresenti().add(azienda);
        return this;
    }
}
