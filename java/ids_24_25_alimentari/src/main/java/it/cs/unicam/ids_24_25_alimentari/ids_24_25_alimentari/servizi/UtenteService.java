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
import java.util.NoSuchElementException;
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
    public Optional<Utente> getUtenteById(long id) {
        Utente utente = utenteRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Utente con id " + id + " non trovato"));;
        return Optional.ofNullable(utente);
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
     * crea un nuovo account Utente.
     * usa gli stessi parametri per il metodo credenziali base
     * usa inoltre i seguenti parametri:
     * 
     * @param iban  l'iban del curatore
     * @param carta la carta d'identità del curatore
     * @param cv    il curriculum del curatore
     */
    public Utente nuovoUtente(String nome, String cognome, String email, String password, String telefono, String iban,Ruolo ruolo,
                            File carta, File cv) {
        credenzialiBase(nome, cognome, email, password, telefono, iban, ruolo);
        builder.costruisciCartaIdentita(carta);
        builder.costruisciCv(cv);
        return utenteRepository.save(builder.getUtente());
    }

    private void aggiungiIndirizzo(Long idUtente, Indirizzo indirizzo, boolean isSpedizione) {
            if (idUtente == null) {
                throw new IllegalArgumentException("ID utente non può essere null");
            }
            if (indirizzo == null) {
                throw new IllegalArgumentException("Indirizzo non può essere null");
            }

            Indirizzo indirizzoSalvato = indirizzoService.save(indirizzo);

            Optional<Utente> utente = getUtenteById(idUtente);
            if (utente.isPresent()) {
                if (isSpedizione) {
                    utente.get().setIndirizzoSpedizione(indirizzoSalvato);
                } else {
                    utente.get().setIndirizzoFatturazione(indirizzoSalvato);
                }
                utenteRepository.save(utente.get());
            } else {
                throw new NoSuchElementException("Utente con id " + idUtente + " non trovato");
            }
        }

    /**
     * <h2>Aggiunge un indirizzo di spedizione all'utente</h2>
     * <p>
     * Questo metodo consente di aggiungere un indirizzo di spedizione a un utente
     * esistente nel sistema. L'indirizzo viene salvato nel database e associato
     * all'utente specificato.
     * </p>
     *
     * @param idUtente  L'ID dell'utente a cui aggiungere l'indirizzo.
     * @param indirizzo L'indirizzo di spedizione da aggiungere.
     * @throws IllegalArgumentException Se l'ID utente o l'indirizzo sono null.
     * @throws NoSuchElementException   Se l'utente con l'ID specificato non è trovato.
     */
    public void aggiungiIndirizzoSpedizione(Long idUtente, Indirizzo indirizzo) {
        aggiungiIndirizzo(idUtente, indirizzo, true);
    }

    /**
     * <h2>Aggiunge un indirizzo di fatturazione all'utente</h2>
     * <p>
     * Questo metodo consente di aggiungere un indirizzo di fatturazione a un utente
     * esistente nel sistema. L'indirizzo viene salvato nel database e associato
     * all'utente specificato.
     * </p>
     *
     * @param idUtente  L'ID dell'utente a cui aggiungere l'indirizzo.
     * @param indirizzo L'indirizzo di fatturazione da aggiungere.
     * @throws IllegalArgumentException Se l'ID utente o l'indirizzo sono null.
     * @throws NoSuchElementException   Se l'utente con l'ID specificato non è trovato.
     */
    public void aggiungiIndirizzoFatturazione(Long idUtente, Indirizzo indirizzo) {
        aggiungiIndirizzo(idUtente, indirizzo, false);
    }
}
