package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.Eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.Evento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.StatusEvento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;

import java.io.File;
import java.time.LocalDateTime;

public abstract class EventoBuilder<T extends Evento, B extends EventoBuilder<T, B>> {

    protected T evento;

    protected EventoBuilder() { // costruttore protetto
        reset();
    }

    /**
     * Metodo di reset dei Builder.
     * permette di creare una nuova istanza builder
     * 
     * @return una nuova istanza vuota Builder per il costruttore
     */
    @SuppressWarnings("unchecked")
    public B reset() {
        this.evento = nuovaIstanza();
        return (B) this;
    }

    protected abstract T nuovaIstanza();

    /* ---------- campi comuni ---------- */
    public B costruisciTitolo(String titolo) {
        evento.setTitolo(titolo);
        return (B) this;
    }

    public B costruisciDescrizione(String descrizione) {
        evento.setDescrizione(descrizione);
        return (B) this;
    }

    public B costruisciStatus(StatusEvento status) {
        evento.setStatusEvento(status);
        return (B) this;
    }

    public B costruisciInizio(LocalDateTime inizio) {
        evento.setInizio(inizio);
        return (B) this;
    }

    public B costruisciFine(LocalDateTime fine) {
        evento.setFine(fine);
        return (B) this;
    }

    public B costruisciLocandina(File locandina) {
        evento.setLocandina(locandina);
        return (B) this;
    }

    public B costruisciIndirizzo(Indirizzo indirizzo) {
        evento.setIndirizzo(indirizzo);
        return (B) this;
    }

    public B costruisciCreatore(Utente creatore) {
        evento.setCreatore(creatore);
        return (B) this;
    }
    /* ---------------------------------- */

    /** Restituisce l’istanza pronta all’uso */
    public T build() {
        T eventoCreato = this.evento;
        this.reset();
        return eventoCreato;
    }
}