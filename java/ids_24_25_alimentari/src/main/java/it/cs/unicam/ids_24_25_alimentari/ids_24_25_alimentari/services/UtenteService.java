package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.UtenteDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.UtenteBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.UtenteRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class UtenteService {
    @Autowired
    private final UtenteRepository utenteRepository;
    private final UtenteBuilder builder;
    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
        this.builder = new UtenteBuilder();
    }
    /**
     * ottiene tutti gli utenti del sistema
     *
     * @return gli utenti salvati nel sistema
     */
    public List<Utente> visualizzaUtenti(){
        List<Utente> utenti = new ArrayList<>();
        if (utenti == null)
            throw new IllegalArgumentException("nessun utente trovato");
        utenteRepository.findAll().forEach(utenti::add);
        return utenti;
    }
    public boolean isRegistrato(String email){
        if (email == null)
            throw new IllegalArgumentException("email nulla");
        Utente registrato = utenteRepository.findByEmail(email);
        return (registrato != null && email.equals(registrato.getEmail()));
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
     * Creazione di un nuovo oggetto utente
     * @param nome il nome dell'utente
     * @param cognome il cognome dell'utente
     * @param email l'email dell'utente
     * @param password la password dell'utente
     * @param telefono il telefono dell'utente
     */

    public Utente nuovoUtente(String nome, String cognome, String email, String password, String telefono){
        credenzialiBase(nome, cognome, email, password, telefono);
        builder.costruisciRuolo(Ruolo.ACQUIRENTE);
        return builder.getUtente();
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
     * seleziona l'utente dall'id
     *
     * @return l'utente salvato nel sistema
     */
    public Utente mostraUtente(long id){
        Utente utente = utenteRepository.findById(id);
        if (utente == null)
            throw new IllegalArgumentException("utente non trovato");
        return utente;
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
    /**
     *  overriding del metodo isAutorizzato che effettua il confronto dei ruoli
     * @param attuale il ruolo effettivo dell'utente
     * @param richiesto il ruolo richiesto per l'autorizzazione
     * @return ture se l'autorizzazione richiesta corrisponde a quella attuale,
     *         false altrimenti
     */
    public boolean isAutorizzato(Ruolo attuale, Ruolo richiesto){
        return (attuale.equals(richiesto));
    }
    public void login(){
        //TODO: implementa login
    }
    public void logout(){
        //TODO: implementa logout
    }
    public void registraUtente(UtenteDTO utente){
        if(!AccountManager.isPasswordValid(utente.password()))
            throw new IllegalArgumentException("Password non valida");
        if(isRegistrato(utente.email()))
            throw new IllegalArgumentException("utente già registrato");
        nuovoUtente(utente.nome(), utente.cognome(), utente.email(), utente.password(), utente.telefono());
    }
}
