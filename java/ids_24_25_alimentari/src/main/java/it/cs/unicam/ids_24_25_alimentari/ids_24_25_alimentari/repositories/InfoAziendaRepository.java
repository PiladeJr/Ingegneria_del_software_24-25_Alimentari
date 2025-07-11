package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info.InfoAzienda;

import java.util.Optional;

@Repository
public interface InfoAziendaRepository extends JpaRepository<InfoAzienda, Long> {
    @Query("SELECT i FROM InfoAzienda i WHERE i.id =?1 AND i.status = 'APPROVATO'")
    Optional<InfoAzienda> findByIdAndApprovato(Long id);
    @Query("SELECT i FROM InfoAzienda i WHERE i.azienda =?1 AND i.status = 'APPROVATO'")
    Optional<InfoAzienda> findByAzienda(Long id);
}
