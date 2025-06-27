package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.Richieste;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.Collaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.RichiestaCollaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;

public class RichiestaCollaborazioneBuilder {
    RichiestaCollaborazione richiesta;
    public RichiestaCollaborazioneBuilder() {
        reset();
    }

    public void costruisciRuolo(Ruolo ruolo) {
        richiesta.setRuolo(ruolo);
    }

    public void costruisciCollaborazione(Collaborazione collaborazione) {
        richiesta.setCollaborazione(collaborazione);
    }

    public RichiestaCollaborazione build() {
        RichiestaCollaborazione r = this.richiesta;
        reset();
        return r;
    }

    public void reset() {
        richiesta = new RichiestaCollaborazione();
    }

}
