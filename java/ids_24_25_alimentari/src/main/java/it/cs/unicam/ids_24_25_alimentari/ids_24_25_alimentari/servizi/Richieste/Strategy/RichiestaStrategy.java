package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Strategy;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Tipologia;

public interface RichiestaStrategy {
    void processaRichiesta(Richiesta richiesta);
    Object ottieniRichiesta(Richiesta richiesta);
    Tipologia getTipologia();
}
