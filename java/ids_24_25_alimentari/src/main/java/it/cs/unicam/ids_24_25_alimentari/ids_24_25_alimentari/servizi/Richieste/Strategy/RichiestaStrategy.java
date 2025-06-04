package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Strategy;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.Contenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Tipologia;

public interface RichiestaStrategy {

    /**
     * Processa la richiesta specificata.
     *
     * @param richiesta
     */
    void processaRichiesta(Richiesta richiesta);

    /**
     * Ottiene il contenuto associato alla richiesta.
     * @param richiesta
     *
     * @return Contenuto associato alla richiesta
     */
    Contenuto ottieniRichiesta(Richiesta richiesta);

    /**
     * Restituisce la tipologia della richiesta.
     *
     * @return Tipologia della richiesta
     */
    Tipologia getTipologia();
}
