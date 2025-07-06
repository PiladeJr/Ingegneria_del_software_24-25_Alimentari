package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;

public interface RichiestaInterface {
    void notificaNuovaRichiesta(Ruolo ruolo);

//    void notificaAccettazioneRichiesta(long idRichiesta);
    void notificaRifiutoRichiesta(String rifiuto, String email);

}
