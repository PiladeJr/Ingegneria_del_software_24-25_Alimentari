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
}

