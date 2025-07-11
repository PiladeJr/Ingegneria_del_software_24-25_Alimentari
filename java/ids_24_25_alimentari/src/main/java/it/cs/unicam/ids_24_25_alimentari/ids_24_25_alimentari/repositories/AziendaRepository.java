package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;

import java.util.List;
import java.util.Optional;

@Repository
public interface AziendaRepository extends JpaRepository<Azienda, Long> {
    @Query ("SELECT a FROM Azienda a WHERE a.denominazioneSociale = ?1")
    List<Azienda> findAllByDenominazioneSociale(String denominazione);
    @Query("SELECT a FROM Azienda a WHERE a.utente.ruolo = :ruolo")
    List<Azienda> findAllByRuolo(Ruolo ruolo);
    @Query("SELECT a FROM Azienda a WHERE a.utente.ruolo = :ruolo AND a.id = :idAzienda")
    Azienda findAziendaByIdAndRuolo(long idAzienda, Ruolo ruolo);
    @Query("SELECT a FROM Azienda a WHERE a.utente.id = :idUtente")
    Optional<Azienda> findAziendaByUtenteId(long idUtente);
}
