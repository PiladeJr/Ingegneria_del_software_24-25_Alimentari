package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Tipologia;

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

    public RichiestaBuilder costruisciTipoContenuto(String tipoContenuto) {
        richiesta.setTipoContenuto(tipoContenuto);
        return this;
    }

    public RichiestaBuilder costruisciApprovato(Boolean approvato) {
        richiesta.setApprovato(approvato);
        return this;
    }

    public RichiestaBuilder costruisciIdMittente(long idMittente) {
        richiesta.setIdMittente(idMittente);
        return this;
    }

    public RichiestaBuilder costruisciIdCuratore(long idCuratore) {
        richiesta.setIdCuratore(idCuratore);
        return this;
    }

    public RichiestaBuilder costruisciTargetId(long targetId) {
        richiesta.setTargetId(targetId);
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
