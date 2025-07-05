package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.utente.UtenteDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.UtenteBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.UtenteRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private final IndirizzoService indirizzoService;
    UtenteBuilder builder;

    public UtenteService(UtenteRepository utenteRepository, IndirizzoService indirizzoService) {
        this.utenteRepository = utenteRepository;
        this.indirizzoService = indirizzoService;
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
     * seleziona l'utente dall'id
     *
     * @return l'utente salvato nel sistema
     */
    public Utente getUtenteById(long id) {
        Utente utente = utenteRepository.findById(id);
        if (utente == null)
            throw new IllegalArgumentException("utente non trovato");
        return utente;
    }

    /**
     * ottieni l'id dell'utente autenticato estraendolo dal suo token
     * 
     * @return l'id dell'utente autenticato
     */
    public Long getIdUtenteAutenticato() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            Optional<Utente> utente = utenteRepository.findByEmail(userDetails.getUsername());
            return utente.isPresent() ? utente.get().getId() : null;
        }
        throw new RuntimeException("Utente non autenticato");
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
     * restituisce il ruolo dell'utente
     *
     * @return ruolo se l'utente esiste, null altrimenti
     */
    public Ruolo getRuoloUtenteById(Long idUtente) {
        Optional<Utente> utente = utenteRepository.findById(idUtente);
        return utente.isPresent() ? utente.get().getRuolo() : null;
    }

    /**
     * <h2>Restituisce una lista di email degli utenti che hanno un determinato
     * ruolo.</h2>
     *
     * @param ruolo - il ruolo degli utenti di cui si vogliono ottenere le email
     * @return una lista di email degli utenti con il ruolo specificato
     */
    public List<String> getEmailsByRuolo(Ruolo ruolo) {
        return utenteRepository.findEmailsByRuolo(ruolo);
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
    private void credenzialiBase(String nome, String cognome, String email, String password, String telefono,
            String iban, Ruolo ruolo) {
        builder.costruisciNome(nome);
        builder.costruisciCognome(cognome);
        builder.costruisciEmail(email);
        builder.costruisciPassword(password);
        builder.costruisciTelefono(telefono);
        builder.costruisciIban(iban);
        builder.costruisciRuolo(ruolo);
    }

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
        credenzialiBase(nome, cognome, email, password, telefono, iban, Ruolo.ANIMATORE);
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
    public void nuovoAzienda(String nome, String cognome, String email, String password, String telefono, Ruolo ruolo,
            String iban, long idAzienda,
            File cartaIdentita) {
        credenzialiBase(nome, cognome, email, password, telefono, iban, ruolo);
        builder.costruisciIdAzienda(idAzienda);
        builder.costruisciCartaIdentita(cartaIdentita);
        utenteRepository.save(builder.getUtente());
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
        credenzialiBase(nome, cognome, email, password, telefono, iban, Ruolo.CURATORE);
        builder.costruisciCartaIdentita(carta);
        builder.costruisciCv(cv);
        utenteRepository.save(builder.getUtente());
    }

    /**
     * Aggiunge un indirizzo di spedizione all'utente
     *
     * @param idUtente  l'id dell'utente a cui aggiungere l'indirizzo
     * @param indirizzo l'indirizzo di spedizione da aggiungere
     */
    public void aggiungiIndirizzoSpedizione(Long idUtente, Indirizzo indirizzo) {
        if (idUtente == null) {
            throw new IllegalArgumentException("ID utente non può essere null");
        }
        if (indirizzo == null) {
            throw new IllegalArgumentException("Indirizzo non può essere null");
        }

        Indirizzo indirizzoSalvato = indirizzoService.save(indirizzo);

        Utente utente = getUtenteById(idUtente);
        utente.setIndirizzoSpedizione(indirizzoSalvato);
        utenteRepository.save(utente);
    }

    /**
     * Aggiunge un indirizzo di fatturazione all'utente
     *
     * @param idUtente  l'id dell'utente a cui aggiungere l'indirizzo
     * @param indirizzo l'indirizzo di fatturazione da aggiungere
     */
    public void aggiungiIndirizzoFatturazione(Long idUtente, Indirizzo indirizzo) {
        if (idUtente == null) {
            throw new IllegalArgumentException("ID utente non può essere null");
        }
        if (indirizzo == null) {
            throw new IllegalArgumentException("Indirizzo non può essere null");
        }
        Indirizzo indirizzoSalvato = indirizzoService.save(indirizzo);

        Utente utente = getUtenteById(idUtente);
        utente.setIndirizzoFatturazione(indirizzoSalvato);
        utenteRepository.save(utente);
    }
}
