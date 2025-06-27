package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.RichiestaContenuto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.Tipologia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RichiestaContenutoRepository extends JpaRepository<RichiestaContenuto, Long> {
    @Query("SELECT r FROM RichiestaContenuto r")
    List<RichiestaContenuto> getAllRichiesteContenuto();
    @Query("SELECT r FROM RichiestaContenuto r WHERE r.tipologia = ?1")
    List<RichiestaContenuto> getRichiesteByTipo(Tipologia tipologia);
    @Query("SELECT r FROM RichiestaContenuto r WHERE r.id = ?1")
    RichiestaContenuto getRichiestaById(Long id);

    /*
     * public Richiesta findRichiestaByUtenteId(Long idMittente);
     */
}
