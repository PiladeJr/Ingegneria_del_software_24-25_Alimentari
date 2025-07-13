package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto.StrategyContenuto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.Contenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.RichiestaContenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.Tipologia;
import org.springframework.http.ResponseEntity;

public interface RichiestaContenutoStrategy {

    /**
     * Processa la richiesta specificata.
     *
     * @param richiesta
     * @param status
     */
    void processaRichiesta(RichiestaContenuto richiesta, Boolean status);

    /**
     * Ottiene il contenuto associato alla richiesta.
     * @param richiesta
     *
     * @return Contenuto associato alla richiesta
     */
    ResponseEntity<?> visualizzaContenutoByRichiesta(RichiestaContenuto richiesta);

    /**
     * Restituisce la tipologia della richiesta.
     *
     * @return Tipologia della richiesta
     */
    Tipologia getTipologia();
}
