package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.Collaborazione;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollaborazioneRepository extends JpaRepository<Collaborazione, Long> {
    @Query("SELECT c FROM Collaborazione c WHERE c.status != 'ELIMINATO' AND c.id = ?1")
    Optional<Collaborazione> findById(Long id);

    @Query("SELECT c FROM Collaborazione c WHERE c.id = ?1")
    Optional<Collaborazione> findByIdAdmin(Long id);

}
