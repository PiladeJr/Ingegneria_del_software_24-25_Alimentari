package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.RichiesteCollaborazione.RichiestaCollaborazione;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RichiestaCollaborazioneRepository extends JpaRepository<RichiestaCollaborazione, Long> {
    /*
     * public RichiestaCollaborazione findById(long id);
     * 
     * public RichiestaCollaborazione findByRuolo(Ruolo ruolo);
     */
}
