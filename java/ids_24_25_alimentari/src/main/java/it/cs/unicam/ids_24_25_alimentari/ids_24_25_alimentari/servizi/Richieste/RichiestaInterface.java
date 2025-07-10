package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;

public interface RichiestaInterface {
    void notificaNuovaRichiesta(Ruolo ruolo);
    void notificaApprovazione(String messaggio, String email, String tipologia);
    void notificaRifiuto(String rifiuto, String email, String tipologia);

}
