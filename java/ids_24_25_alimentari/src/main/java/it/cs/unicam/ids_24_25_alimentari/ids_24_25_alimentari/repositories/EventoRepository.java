package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.Evento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.EventoFiera;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.EventoVisita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

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
    @Query("SELECT e FROM Evento e WHERE e.creatore.id = :idUtente AND e.status = 'APPROVATO'")
    List<Evento> findByCreatoreId(Long idUtente);

    /**
     * Restituisce tutti gli eventi visibili sulla piattaforma con stato PROGRAMMATO
     */
    @Query("SELECT e FROM Evento e WHERE e.statusEvento = 'PROGRAMMATO'")
    List<Evento> findAllProgrammati();

    /**
     * Restituisce tutti gli eventi di tipo visita visibili sulla piattaforma
     */
    @Query("SELECT e FROM Evento e WHERE TYPE(e) = EventoVisita AND e.statusEvento = 'PROGRAMMATO'")
    List<EventoVisita> findAllVisitaProgrammati();

    /**
     * Restituisce tutti gli eventi di tipo fiera visibili sulla piattaforma
     */
    @Query("SELECT e FROM Evento e WHERE TYPE(e) = EventoFiera AND e.statusEvento = 'PROGRAMMATO'")
    List<EventoFiera> findAllFieraProgrammati();

    /**
     * Restituisce tutti gli eventi il cui titolo contiene una stringa specifica, ignorando il case.
     */
    @Query("SELECT e FROM Evento e WHERE LOWER(e.titolo) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Evento> findByTitleContainingParameter(String title);

    /**
     * Restituisce tutti gli eventi programmati il cui titolo contiene una stringa specifica, ignorando il case.
     */
    @Query("SELECT e FROM Evento e WHERE LOWER(e.titolo) LIKE LOWER(CONCAT('%', :title, '%')) AND e.statusEvento = 'PROGRAMMATO'")
    List<Evento> findByTitleContainingParameterAndStatus(String title);

    /**
     * Restituisce tutti gli eventi il cui titolo contiene una stringa specifica, ignorando il case, e sono stati creati da un utente specifico.
     */
    @Query("SELECT e FROM Evento e WHERE LOWER(e.titolo) LIKE LOWER(CONCAT('%', :title, '%')) AND e.creatore.id = :id AND e.status = 'APPROVATO'")
    List<Evento> findByTitleContainingParameterAndCreatoreId(String title, Long id);

    @Query("SELECT e FROM Evento e WHERE TYPE(e) = EventoVisita AND e.creatore.id = :id AND e.status = 'APPROVATO'")
    List<EventoVisita> findAllVisitaByCreatore(Long id);

    @Query("SELECT e FROM Evento e WHERE TYPE(e) = EventoFiera AND e.creatore.id = :id AND e.status = 'APPROVATO'")
    List<EventoFiera>findAllFieraByCreatore(long id);

    @Query("SELECT e FROM Evento e  WHERE e.id = :id AND e.statusEvento = 'PROGRAMMATO'")
    Optional<Evento> findByIdAndProgrammato(Long id);

    @Query("SELECT e FROM Evento e WHERE e.id = :id AND e.status!= 'ELIMINATO'")
    Optional<Evento> findByIdNonAutorizzato(Long id);
}

