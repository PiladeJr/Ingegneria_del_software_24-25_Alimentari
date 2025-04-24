package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi.EventoVisita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoVisitaRepository extends JpaRepository<EventoVisita,Long> {
    @Query("SELECT e FROM Evento e WHERE TYPE(e) = EventoVisita")
    List<EventoVisita> findAllVisita();

    @Query("SELECT e FROM Evento e WHERE TYPE(e) = EventoVisita")
    List<EventoVisita> findAllFiera();
}
