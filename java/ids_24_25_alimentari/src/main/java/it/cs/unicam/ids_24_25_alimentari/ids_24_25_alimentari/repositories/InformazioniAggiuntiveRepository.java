package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.InformazioniAggiuntive;

@Repository
public interface InformazioniAggiuntiveRepository
        extends JpaRepository<InformazioniAggiuntive, Long> {
    /* public RichiestaInformazioniAggiuntive findById(long id); */

}
