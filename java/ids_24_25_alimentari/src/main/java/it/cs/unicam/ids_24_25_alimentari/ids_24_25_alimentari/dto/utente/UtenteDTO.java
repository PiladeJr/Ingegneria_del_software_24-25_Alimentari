package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.utente;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;

public record UtenteDTO (String nome, String cognome, String email, String password, String telefono, Ruolo ruolo){
}
