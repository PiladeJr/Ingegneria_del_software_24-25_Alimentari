package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.Tipologia;

/**
 * -- GETTER --
 *  metodo che restituisce l'oggetto utente creato
 *
 * @return l'oggetto Richiesta creata
 */
public class RichiestaBuilder {
     Richiesta richiesta;
     public RichiestaBuilder() {
        reset();
    }

    public RichiestaBuilder costruisciTipologia(Tipologia tipologia) {
        richiesta.setTipologia(tipologia);
        return this;
    }

    public RichiestaBuilder costruisciApprovato(Boolean approvato) {
        richiesta.setApprovazione(approvato);
        return this;
    }

    public RichiestaBuilder costruisciIdMittente(long idMittente) {
        richiesta.setIdMittente(idMittente);
        return this;
    }

    public RichiestaBuilder costruisciIdInformazioni(long idInformazioni) {
        richiesta.setIdInformazioni(idInformazioni);
        return this;
    }

    public RichiestaBuilder costruisciIdCuratore(long idCuratore) {
        richiesta.setIdCuratore(idCuratore);
        return this;
    }

    public Richiesta build() {
        Richiesta r = this.richiesta;
        reset();
        return r;
    }

    public void reset() {
        richiesta = new Richiesta();
    }
}
