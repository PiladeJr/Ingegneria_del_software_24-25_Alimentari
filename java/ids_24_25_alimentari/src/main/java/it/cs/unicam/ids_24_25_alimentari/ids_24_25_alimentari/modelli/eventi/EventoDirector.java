package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.Eventi.EventoBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.Eventi.FieraBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.Eventi.VisitaBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class EventoDirector {

    /*
     * ===============================================================
     * builder concreti ri‑utilizzabili (una sola istanza ciascuno)
     * ===============================================================
     */
    private final FieraBuilder fieraBuilder = new FieraBuilder();
    private final VisitaBuilder visitaBuilder = new VisitaBuilder();
    /*
     * ===============================================================
     * builder “corrente” – può essere cambiato a runtime
     * ===============================================================
     */
    private EventoBuilder<? extends Evento, ?> builderCorrente;

    public EventoDirector() {
        this.builderCorrente = fieraBuilder; // default
    }

    public EventoDirector(EventoBuilder<? extends Evento, ?> builder) {
        this.builderCorrente = builder;
    }

    /** Permette di cambiare builder a runtime */
    public void cambiaBuilder(EventoBuilder<? extends Evento, ?> nuovo) {
        this.builderCorrente = Objects.requireNonNull(nuovo);
    }

    /**
     * costruzione base per gli eventi
     * imposta i dati comuni ad entrambe le tipologie di eventi
     * e restituisce il builder
     * così da poter concatenare i passi specifici prima della build().
     *
     * @param titolo il titolo dell'evento
     * @param descr  la descrizione dell'evento
     * @param inizio la data e ora di inizio evento
     * @param fine   la data e ora di fine evento
     * @param file   l'immagine della locandina dell'evento
     * @param luogo  l'indirizzo dell'evento
     * @return
     * @param <T> un oggetto evento con le credenziali base
     */
    private <T extends Evento, B extends EventoBuilder<T, B>> B creaBase(
            B builder,
            String titolo,
            String descr,
            LocalDateTime inizio,
            LocalDateTime fine,
            File file,
            Indirizzo luogo,
            Utente utente) {

        builder.reset()
                .costruisciTitolo(titolo)
                .costruisciDescrizione(descr)
                .costruisciStatus(StatusEvento.PROPOSTO)
                .costruisciInizio(inizio)
                .costruisciFine(fine)
                .costruisciLocandina(file)
                .costruisciIndirizzo(luogo)
                .costruisciCreatore(utente);
        return builder;
    }

    /** Esempio di configurazione specifica per una Fiera */
    public EventoFiera creaFieraCompleta(
            String titolo,
            String descr,
            LocalDateTime inizio,
            LocalDateTime fine,
            File file,
            Indirizzo luogo,
            Utente creatore,
            List<Azienda> aziende) {
        cambiaBuilder(fieraBuilder);
        if (!(builderCorrente instanceof FieraBuilder fieraBuilder))
            throw new IllegalStateException("Builder non compatibile");
        return creaBase(fieraBuilder, titolo, descr, inizio, fine, file, luogo, creatore)
                .costruisciAziende(aziende)
                .build();
    }

    public EventoVisita creaVisitaCompleta(
            String titolo,
            String descr,
            LocalDateTime inizio,
            LocalDateTime fine,
            File file,
            Indirizzo luogo,
            Utente creatore,
            Azienda azienda) {
        cambiaBuilder(visitaBuilder);
        if (!(builderCorrente instanceof VisitaBuilder visitaBuilder))
            throw new IllegalStateException("Builder non compatibile");

        return creaBase(visitaBuilder, titolo, descr, inizio, fine, file, luogo, creatore)
                .costruisciAzienda(azienda)
                .costruisciIscritti(null)
                .build();
    }

}
