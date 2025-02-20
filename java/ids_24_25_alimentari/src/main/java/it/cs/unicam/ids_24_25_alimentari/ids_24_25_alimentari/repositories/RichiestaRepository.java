package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Richiesta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RichiestaRepository extends JpaRepository<Richiesta, Long> {
    /*
     * public Richiesta findRichiestaById(Long id);
     * 
     * public Richiesta findRichiestaByUtenteId(Long idMittente);
     * 
     * public Richiesta findRichiestaByTipologia(Tipologia tipologia);
     */
}
