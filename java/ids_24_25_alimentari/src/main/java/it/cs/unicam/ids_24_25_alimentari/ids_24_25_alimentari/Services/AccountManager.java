package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Services;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Models.Utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Models.Utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Models.Utente.UtenteBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Repositories.UtenteRepository;

import java.io.File;

/**
 * classe con i metodi responsabili per la creazione e gestione
 * dell'account utente
 */

public class AccountManager {
    private final UtenteRepository utenteRepository;
    private final UtenteBuilder builder;

    public AccountManager(UtenteRepository utenteRepository, UtenteBuilder builder) {
        this.utenteRepository = utenteRepository;
        this.builder = builder;
    }

    /**
     * costruzione delle credenziali base dell'utente
     * @param nome il nome dell'utente
     * @param cognome il cognome dell'utente
     * @param email l'email dell'utente
     * @param password la password dell'utente
     * @param telefono il telefono dell'utente
     */
    private void credenzialiBase(String nome, String cognome, String email, String password, String telefono){
        builder.costruisciNome(nome);
        builder.costruisciCognome(cognome);
        builder.costruisciEmail(email);
        builder.costruisciPassword(password);
        builder.costruisciTelefono(telefono);
    }
    /**
     * Crea un nuovo utente nel sistema
     * @param nome il nome dell'utente
     * @param cognome il cognome dell'utente
     * @param email l'email dell'utente
     * @param password la password dell'utente
     * @param telefono il telefono dell'utente
     */

    public void nuovoUtente(String nome, String cognome, String email, String password, String telefono){
        credenzialiBase(nome, cognome, email, password, telefono);
        builder.costruisciRuolo(Ruolo.ACQUIRENTE);
        utenteRepository.save(builder.getUtente());
    }
    /**
     * crea un nuovo account di tipo animatore
     * usa gli stessi parametri per il metodo credenziali base
     * usa inoltre i seguenti parametri:
     * @param iban l'iban dell'animatore
     * @param carta il file contenente la carta d'identità dell'animatore
     */
    public void nuovoAnimatore(String nome, String cognome, String email, String password, String telefono, String iban, File carta){
        credenzialiBase(nome, cognome, email, password, telefono);
        builder.costruisciRuolo(Ruolo.ANIMATORE);
        builder.costruisciIban(iban);
        builder.costruisciCartaIdentita(carta);
        utenteRepository.save(builder.getUtente());
    }
    /**
     * crea un nuovo account di tipo produttore, trasformatore o  distruibutore,
     * in base al ruolo selezionato dall'utente.
     * usa gli stessi parametri per il metodo credenziali base
     * usa inoltre i seguenti parametri:
     * @param ruolo il ruolo selezionato dall'utente (PRODUTTORE, TRASFORMATORE, DISTRIBUTORE)
     * @param carta il file contenente la carta d'identità dell'utente
     */
    public void nuovoAzienda(String nome, String cognome, String email, String password, String telefono, Ruolo ruolo, File carta){
        credenzialiBase(nome, cognome, email, password, telefono);
        builder.costruisciRuolo(ruolo);
        builder.costruisciCartaIdentita(carta);
        utenteRepository.save(builder.getUtente());
    }
    /**
     * crea un nuovo account di tipo curatore.
     * usa gli stessi parametri per il metodo credenziali base
     * usa inoltre i seguenti parametri:
     * @param iban l'iban del curatore
     * @param carta la carta d'identità del curatore
     * @param cv il curriculum del curatore
     */
    public void nuovoCuratore(String nome, String cognome, String email, String password, String telefono, String iban, File carta, File cv){
        credenzialiBase(nome, cognome, email, password, telefono);
        builder.costruisciRuolo(Ruolo.CURATORE);
        builder.costruisciIban(iban);
        builder.costruisciCartaIdentita(carta);
        builder.costruisciCv(cv);
        utenteRepository.save(builder.getUtente());
    }
    public boolean checkPassword(String password, String confermaPassword){
        return password.equals(confermaPassword);
    }
    public boolean isRegistrato(String email){
        Utente registrato = utenteRepository.findByEmail(email);
        return email.equals(registrato.getEmail());
    }
}
