package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.Richieste;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.RichiestaContenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.Tipologia;

public class RichiestaContenutoBuilder {
     RichiestaContenuto richiesta;
     public RichiestaContenutoBuilder() {
        reset();
    }

    public void costruisciTipologia(Tipologia tipologia) {
        richiesta.setTipologia(tipologia);
    }

    public void costruisciTipoContenuto(String tipoContenuto) {
        richiesta.setTipoContenuto(tipoContenuto);
    }

    public void costruisciIdMittente(long idMittente) {
        richiesta.setIdMittente(idMittente);
    }

    public void costruisciTargetId(long targetId) {
        richiesta.setTargetId(targetId);
    }

    public RichiestaContenuto build() {
        RichiestaContenuto r = this.richiesta;
        reset();
        return r;
    }

    public void reset() {
        richiesta = new RichiestaContenuto();
    }
}
