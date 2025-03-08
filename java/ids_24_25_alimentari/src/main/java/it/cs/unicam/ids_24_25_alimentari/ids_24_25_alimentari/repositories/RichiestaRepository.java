package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.Richiesta;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.Tipologia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RichiestaRepository extends JpaRepository<Richiesta, Long> {
    @Query("SELECT r FROM Richiesta r")
    List<Richiesta> getAllRichiesteContenuto();
    @Query("SELECT r FROM Richiesta r WHERE r.tipologia = ?1")
    List<Richiesta> getRichiesteByTipo(Tipologia tipologia);


    /*
     * public Richiesta findRichiestaById(Long id);
     * 
     * public Richiesta findRichiestaByUtenteId(Long idMittente);
     * 
     * public Richiesta findRichiestaByTipologia(Tipologia tipologia);
     */
}
