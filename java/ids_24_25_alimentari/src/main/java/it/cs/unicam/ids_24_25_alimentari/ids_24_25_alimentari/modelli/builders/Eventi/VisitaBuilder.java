package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.Eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoFiera;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoVisita;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;

import java.util.ArrayList;
import java.util.List;

/* ---- EventoVisita ------- */
public class VisitaBuilder extends EventoBuilder<EventoVisita, VisitaBuilder> {
    public VisitaBuilder() {
        super();
    }
    @Override
    protected EventoVisita nuovaIstanza() {
        return new EventoVisita();
    }
    public VisitaBuilder costruisciAzienda(Azienda azienda) {
        evento.setAziendaRiferimento(azienda);
        return this;
    }

    public VisitaBuilder costruisciIscritti(List<Utente> utenti) {
        if (utenti == null) {
            evento.setIscritti(new ArrayList<>());
        }
        else{
            evento.setIscritti(new ArrayList<>(utenti));
        }
        return this;
    }

    public VisitaBuilder aggiungiIscritto(Utente utente) {
        if (evento.getIscritti() == null)
            evento.setIscritti(new ArrayList<>());
        evento.getIscritti().add(utente);
        return this;
    }
}