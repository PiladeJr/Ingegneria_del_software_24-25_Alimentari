package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.Evento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoFiera;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoVisita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    /**
     * Restituisce tutti gli eventi ordinati alfabeticamente per titolo.
     */
    List<Evento> findAllByOrderByTitoloAsc();
    /**
     * Restituisce tutti gli eventi ordinati per data di inizio decrescente.
     */
    List<Evento> findAllByOrderByInizioDesc();

    /**
     * Restituisce tutti gli eventi di tipo visita.
     */
    @Query("SELECT e FROM Evento e WHERE TYPE(e) = EventoVisita")
    List<EventoVisita> findAllVisita();

    /**
     * Restituisce tutti gli eventi di tipo fiera.
     */
    @Query("SELECT e FROM Evento e WHERE TYPE(e) = EventoFiera")
    List<EventoFiera> findAllFiera();

    /**
     * Restituisce tutti gli eventi creati da un utente specifico.
     */
    @Query("SELECT e FROM Evento e WHERE e.creatore.id = :id")
    List<Evento> findByCreatoreId(Long idUtente);

    /**
     * Restituisce tutti gli eventi di tipo visita ordinati per data decrescente.
     */
    @Query("SELECT e FROM Evento e WHERE TYPE(e) = EventoVisita ORDER BY e.inizio DESC")
    List<EventoVisita> findAllVisitaOrderByInizioDesc();

    /**
     * Restituisce tutti gli eventi di tipo fiera ordinati per data decrescente.
     */
    @Query("SELECT e FROM Evento e WHERE TYPE(e) = EventoFiera ORDER BY e.inizio DESC")
    List<EventoFiera> findAllFieraOrderByInizioDesc();

    /**
     * Restituisce tutti gli eventi visibili sulla piattaforma con stato PROGRAMMATO
     */
    @Query("SELECT e FROM Evento e WHERE e.status = 1")
    List<Evento> findAllEventiProgrammati();

    /**
     * Restituisce tutti gli eventi ordinati per data di inizio decrescente.
     */
    @Query("SELECT e FROM Evento e WHERE e.status = 1 ORDER BY e.inizio DESC")
    List<Evento> findAllEventiProgrammatiByInizio();
    /**
     * Restituisce tutti gli eventi di tipo visita visibili sulla piattaforma
     */
    @Query("SELECT e FROM Evento e WHERE TYPE(e) = EventoVisita AND e.status = 1")
    List<EventoVisita> findAllVisitaProgrammati();

    /**
     * Restituisce tutti gli eventi di tipo fiera visibili sulla piattaforma
     */
    @Query("SELECT e FROM Evento e WHERE TYPE(e) = EventoFiera AND e.status = 1")
    List<EventoFiera> findAllFieraProgrammati();

    /**
     * Restituisce tutti gli eventi di tipo visita visibili sulla piattaforma ordinati per data decrescente
     */
    @Query("SELECT e FROM Evento e WHERE TYPE(e) = EventoVisita AND e.status = 1 ORDER BY e.inizio DESC")
    List<EventoVisita> findAllVisitaProgrammatiByInizio();

    /**
     * Restituisce tutti gli eventi di tipo fiera visibili sulla piattaforma ordinati per data decrescente
     */
    @Query("SELECT e FROM Evento e WHERE TYPE(e) = EventoFiera AND e.status = 1 ORDER BY e.inizio DESC")
    List<EventoFiera> findAllFieraProgrammatiByInizio();

    /**
     * Restituisce tutti gli eventi il cui titolo contiene una stringa specifica, ignorando il case.
     */
    @Query("SELECT e FROM Evento e WHERE LOWER(e.titolo) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Evento> findByTitleContainingParameter(String title);
    /**
     * Restituisce tutti gli eventi programmati il cui titolo contiene una stringa specifica, ignorando il case.
     */
    @Query("SELECT e FROM Evento e WHERE LOWER(e.titolo) LIKE LOWER(CONCAT('%', :title, '%')) AND e.status = 1")
    List<Evento> findByTitleContainingParameterAndStatus(String title);
    /**
     * Restituisce tutti gli eventi il cui titolo contiene una stringa specifica, ignorando il case, e sono stati creati da un utente specifico.
     */
    @Query("SELECT e FROM Evento e WHERE LOWER(e.titolo) LIKE LOWER(CONCAT('%', :title, '%')) AND e.creatore.id = :id")
    List<Evento> findByTitleContainingParameterAndCreatoreId(String title, Long id);
}

