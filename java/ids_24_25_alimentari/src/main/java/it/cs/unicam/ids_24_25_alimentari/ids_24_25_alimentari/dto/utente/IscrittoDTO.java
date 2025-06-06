package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.utente;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import lombok.Getter;

@Getter
public class IscrittoDTO {
    private String nome;
    private String cognome;
    private String email;

    public IscrittoDTO(Utente utente) {
        this.nome = utente.getNome();
        this.cognome = utente.getCognome();
        this.email = utente.getEmail();
    }

}
