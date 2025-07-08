package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.InformazioniAggiuntive;

import java.util.Optional;

@Repository
public interface InformazioniAggiuntiveRepository extends JpaRepository<InformazioniAggiuntive, Long> {
    @Query("SELECT i FROM InformazioniAggiuntive i WHERE i.id =?1 AND i.approvato = true")
    Optional<InformazioniAggiuntive> findByIdAndApprovato(Long id);
    @Query("SELECT i FROM InformazioniAggiuntive i WHERE i.azienda =?1 ")
    Optional<InformazioniAggiuntive> findByAzienda(Long id);
}
