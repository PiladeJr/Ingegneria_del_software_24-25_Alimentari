package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoFiera;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoVisita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoVisitaRepository extends JpaRepository<EventoVisita,Long> {
    List<EventoVisita> findAll();
    @Query("SELECT e FROM EventoVisita e WHERE e.creatore.id = :id")
    List<EventoVisita> findByCreatoreId(Long id);
    @Query("SELECT v FROM EventoVisita v ORDER BY v.inizio DESC")
    List<EventoVisita> findAllOrderByInizioDesc();
}
