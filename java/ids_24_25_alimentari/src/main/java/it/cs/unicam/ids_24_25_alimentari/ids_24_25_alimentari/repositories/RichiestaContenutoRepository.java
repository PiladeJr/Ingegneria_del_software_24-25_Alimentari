package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.RichiestaContenuto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RichiestaContenutoRepository extends JpaRepository<RichiestaContenuto, Long> {
    @Query("SELECT r FROM RichiestaContenuto r WHERE r.status != 'ELIMINATA'")
    List<RichiestaContenuto> findAllRichieste();
    @Query("SELECT r FROM RichiestaContenuto r WHERE r.id = ?1 AND r.status != 'ELIMINATA'")
    Optional<RichiestaContenuto> findById(Long id);

}
