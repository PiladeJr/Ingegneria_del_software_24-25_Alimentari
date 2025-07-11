package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.RichiestaCollaborazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RichiestaCollaborazioneRepository extends JpaRepository<RichiestaCollaborazione, Long> {
    @Query("SELECT r FROM RichiestaCollaborazione r WHERE r.status != 'ELIMINATO'")
    List<RichiestaCollaborazione> findAll();

    @Query("SELECT r FROM RichiestaCollaborazione r WHERE r.status != 'ELIMINATO' AND r.id = ?1")
    Optional<RichiestaCollaborazione> findById(Long id);
    @Query("SELECT r FROM RichiestaCollaborazione r ")
    List<RichiestaCollaborazione> findAllAdmin();

    @Query ("SELECT r FROM RichiestaCollaborazione r WHERE r.id = ?1")
    Optional<RichiestaCollaborazione> findByIdAdmin(Long id);
}
