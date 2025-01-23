package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Services;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Models.Utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Models.Utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UtenteService {
    @Autowired
    private final UtenteRepository utenteRepository;
    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
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

//        /**
//         * salva l'utente nella database
//         * @param utente l'utente salvato su database
//         */
//        public void addUtente(Utente utente){
//            utenteRepository.save(utente);
//        }
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
    public void register(){
        //TODO: implementa registrazione
    }
}
