package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.UtenteDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders.UtenteBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.UtenteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UtenteService implements UserDetailsService {
    @Autowired
    private final UtenteRepository utenteRepository;
    UtenteBuilder builder;

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
        this.builder = new UtenteBuilder();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Utente> utente = utenteRepository.findByEmail(email);

        Utente user = utente.orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));

        // Converti i ruoli definiti in Utente nel formato compatibile con Spring
        // Security
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRuolo().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(), // Assumi che la password sia già crittografata
                authorities);
    }

    /**
     * seleziona l'utente dalla lista tramite il suo id
     *
     * @param idUtente
     * @return
     */
    public Optional<Utente> selezionaUtenteById(Long idUtente) {
        if (idUtente == null)
            throw new NullPointerException();
        Optional<Utente> user = utenteRepository.findById(idUtente);
        if (user == null)
            throw new NullPointerException();
        return user;
    }

    /**
     * ottiene tutti gli utenti del sistema
     *
     * @return gli utenti salvati nel sistema
     */
    public List<UtenteDTO> visualizzaUtenti() {
        List<UtenteDTO> utentiDTO = new ArrayList<>();
        utenteRepository.findAll().forEach(utente -> {
            UtenteDTO utenteDTO = new UtenteDTO(
                    utente.getNome(),
                    utente.getCognome(),
                    utente.getEmail(),
                    null, // Exclude password
                    utente.getTelefono(),
                    utente.getRuolo());
            utentiDTO.add(utenteDTO);
        });
        return utentiDTO;
    }

    /**
     *
     * verifica che l'utente sia registrato tramite la sua mail
     *
     * @param email cercata nel database
     * @return se l'utente è registrato
     */
    public boolean isRegistrato(String email) {
        if (email == null)
            throw new IllegalArgumentException("email nulla");
        Optional<Utente> registrato = utenteRepository.findByEmail(email);

        return (registrato.isPresent());
    }

    /**
     * costruzione delle credenziali base dell'utente
     * 
     * @param nome     il nome dell'utente
     * @param cognome  il cognome dell'utente
     * @param email    l'email dell'utente
     * @param password la password dell'utente
     * @param telefono il telefono dell'utente
     */
    private void credenzialiBase(String nome, String cognome, String email, String password, String telefono) {
        builder.costruisciNome(nome);
        builder.costruisciCognome(cognome);
        builder.costruisciEmail(email);
        builder.costruisciPassword(password);
        builder.costruisciTelefono(telefono);
    }

    // ---------------------------------da vedere se utilizzato o meno
    /**
     * Creazione di un nuovo oggetto utente
     * 
     * @param nome     il nome dell'utente
     * @param cognome  il cognome dell'utente
     * @param email    l'email dell'utente
     * @param password la password dell'utente
     * @param telefono il telefono dell'utente
     */

    public void nuovoUtente(String nome, String cognome, String email, String password, String telefono) {
        credenzialiBase(nome, cognome, email, password, telefono);
        builder.costruisciRuolo(Ruolo.ACQUIRENTE);
        utenteRepository.save(builder.getUtente());
    }

    // ---------------------------------
    /**
     * crea un nuovo account di tipo animatore
     * usa gli stessi parametri per il metodo credenziali base
     * usa inoltre i seguenti parametri:
     * 
     * @param iban  l'iban dell'animatore
     * @param carta il file contenente la carta d'identità dell'animatore
     */
    public void nuovoAnimatore(String nome, String cognome, String email, String password, String telefono, String iban,
            File carta) {
        credenzialiBase(nome, cognome, email, password, telefono);
        builder.costruisciRuolo(Ruolo.ANIMATORE);
        builder.costruisciIban(iban);
        builder.costruisciCartaIdentita(carta);
        utenteRepository.save(builder.getUtente());
    }

    /**
     * crea un nuovo account di tipo produttore, trasformatore o distruibutore,
     * in base al ruolo selezionato dall'utente.
     * usa gli stessi parametri per il metodo credenziali base
     * usa inoltre i seguenti parametri:
     * 
     * @param ruolo         il ruolo selezionato dall'utente (PRODUTTORE,
     *                      TRASFORMATORE, DISTRIBUTORE)
     * @param cartaIdentita il file contenente la carta d'identità dell'utente
     */
    public void nuovoAzienda(String nome, String cognome, String email, String telefono, Ruolo ruolo, long idAzienda,
            File cartaIdentita, String password) {
        builder.costruisciNome(nome);
        builder.costruisciCognome(cognome);
        builder.costruisciEmail(email);
        builder.costruisciTelefono(telefono);
        builder.costruisciRuolo(ruolo);
        builder.costruisciIdAzienda(idAzienda);
        builder.costruisciCartaIdentita(cartaIdentita);
        builder.costruisciPassword(password);
        utenteRepository.save(builder.getUtente());
    }

    /**
     * seleziona l'utente dall'id
     *
     * @return l'utente salvato nel sistema
     */
    public Utente mostraUtente(long id) {
        Utente utente = utenteRepository.findById(id);
        if (utente == null)
            throw new IllegalArgumentException("utente non trovato");
        return utente;
    }

    /**
     * crea un nuovo account di tipo curatore.
     * usa gli stessi parametri per il metodo credenziali base
     * usa inoltre i seguenti parametri:
     * 
     * @param iban  l'iban del curatore
     * @param carta la carta d'identità del curatore
     * @param cv    il curriculum del curatore
     */
    public void nuovoCuratore(String nome, String cognome, String email, String password, String telefono, String iban,
            File carta, File cv) {
        credenzialiBase(nome, cognome, email, password, telefono);
        builder.costruisciRuolo(Ruolo.CURATORE);
        builder.costruisciIban(iban);
        builder.costruisciCartaIdentita(carta);
        builder.costruisciCv(cv);
        utenteRepository.save(builder.getUtente());
    }
}
