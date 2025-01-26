package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente;

import lombok.Getter;

import java.io.File;

@Getter
public class UtenteBuilder {
    /**
     * -- GETTER --
     *  metodo che restituisce l'oggetto utente creato
     *
     * @return l'oggetto utente creato
     */
    private final Utente utente;

    public UtenteBuilder() {
        this.utente = new Utente();
    }
    public UtenteBuilder costruisciEmail(String email){
        utente.setEmail(email);
        return this;
    }
    public UtenteBuilder costruisciPassword(String password){
        utente.setPassword(password);
        return this;
    }
    public UtenteBuilder costruisciTelefono(String telefono){
        utente.setTelefono(telefono);
        return this;
    }
    public UtenteBuilder costruisciNome(String nome){
        utente.setNome(nome);
        return this;
    }
    public UtenteBuilder costruisciCognome(String cognome){
        utente.setCognome(cognome);
        return this;
    }
    public UtenteBuilder costruisciRuolo(Ruolo ruolo){
        utente.setRuolo(ruolo);
        return this;
    }
    public UtenteBuilder costruisciIban(String iban){
        utente.setIban(iban);
        return this;
    }
    public UtenteBuilder costruisciCv(File cv){
        utente.setCv(cv);
        return this;
    }
    public UtenteBuilder costruisciCartaIdentita(File cartaIdentita){
        utente.setCartaIdentita(cartaIdentita);
        return this;
    }
    public UtenteBuilder rimuoviCv(){
        utente.setCv(null);
        return this;
    }
    public UtenteBuilder rimuoviCartaIdentita(){
        utente.setCartaIdentita(null);
        return this;
    }

}
