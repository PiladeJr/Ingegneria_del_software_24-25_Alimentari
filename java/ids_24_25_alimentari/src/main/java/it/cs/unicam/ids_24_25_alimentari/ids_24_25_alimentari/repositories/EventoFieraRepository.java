package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoFiera;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoVisita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoFieraRepository extends JpaRepository<EventoFiera,Long> {
    List<EventoFiera> findAll();
    @Query("SELECT e FROM EventoFiera e WHERE e.creatore.id = :id")
    List<EventoFiera> findByCreatoreId(Long id);
    @Query("SELECT f FROM EventoFiera f ORDER BY f.inizio DESC")
    List<EventoFiera> findAllOrderByInizioDesc();
}
