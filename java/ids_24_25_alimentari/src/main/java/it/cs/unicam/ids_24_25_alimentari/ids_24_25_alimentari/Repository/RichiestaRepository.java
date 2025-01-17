package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Repository;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Entity.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Enum.Tipologia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RichiestaRepository extends JpaRepository<Richiesta,Long> {
    public Richiesta findRichiestaById(Long id);
    public Richiesta findRichiestaByUtenteId(Long id);
    public Richiesta findRichiestaByTipologia(Tipologia tipologia);
}
