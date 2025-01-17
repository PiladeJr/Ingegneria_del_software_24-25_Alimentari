package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Repository;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Entity.RichiestaInformazioniAggiuntive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RichiestaInformazioniAggiuntiveRepository extends JpaRepository<RichiestaInformazioniAggiuntive,Long> {
    public RichiestaInformazioniAggiuntive findById(long id);

}
