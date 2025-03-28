package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    @Query("SELECT u FROM Utente u WHERE u.id = ?1")
    Utente findById(long id);
    @Query("SELECT u FROM Utente u WHERE u.nome = ?1")
    Utente findByNome(String nome);
    @Query("SELECT u FROM Utente u WHERE u.nome LIKE %?1%")
    List<Utente> findByNomeContaining(String partialName);
    // Recupera tutti gli utenti il cui cognome contiene una specifica stringa
    @Query("SELECT u FROM Utente u WHERE u.cognome LIKE %?1%")
    List<Utente> findByCognomeContaining(String partialCognome);
    @Query("SELECT u FROM Utente u WHERE u.email = ?1")
    Optional<Utente> findByEmail(String email);
    @Query("SELECT u FROM Utente u WHERE u.ruolo = ?1")
    Utente findByRuolo(Ruolo ruolo);
    @Query("SELECT u FROM Utente u WHERE u.ruolo = ?1")
    List<Utente> findAllByRuolo(Ruolo ruolo);
}
