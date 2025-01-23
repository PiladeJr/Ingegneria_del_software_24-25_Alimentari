package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Models.Azienda;

@Repository
public interface AziendaRepository extends JpaRepository<Azienda, Long> {
    /*
     * public Azienda getAziendaById(long id);
     * 
     * public Azienda getAziendaByUtenteId(long idUtente);
     */
}
