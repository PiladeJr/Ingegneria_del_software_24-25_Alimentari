package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;

import java.util.List;

@Repository
public interface AziendaRepository extends JpaRepository<Azienda, Long> {
    @Query("SELECT a FROM Azienda a JOIN Utente u ON a.id = u.idAzienda WHERE u.ruolo = :ruolo")
    List<Azienda> findAziendeByRuolo(Ruolo ruolo);

    @Query("SELECT a FROM Azienda a JOIN Utente u ON a.id = u.idAzienda WHERE u.ruolo = :ruolo AND a.id = :idAzienda")
    Azienda findAziendaByIdAndruolo(long idAzienda, Ruolo ruolo);
}
