package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import lombok.Getter;

import java.io.File;


/**
 * -- GETTER --
 *  metodo che restituisce l'oggetto utente creato
 *
 * @return l'oggetto utente creato
 */
@Getter
public class UtenteBuilder {

    private Utente utente;
    public UtenteBuilder() {
        reset();
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
    //TODO Spostare dal builder di utente a quello di azienda
    public UtenteBuilder costruisciIdAzienda(long idAzienda) {
        utente.setIdAzienda(idAzienda);
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
    public Utente getUtente(){
        Utente u = this.utente;
        reset();
        return u;
    }
    public void reset() {
        this.utente = new Utente();
    }

}
