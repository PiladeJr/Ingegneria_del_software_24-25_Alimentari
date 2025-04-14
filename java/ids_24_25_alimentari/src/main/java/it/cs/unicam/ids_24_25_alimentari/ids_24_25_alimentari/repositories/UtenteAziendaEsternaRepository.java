package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.UtenteAziendaEsterna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtenteAziendaEsternaRepository extends JpaRepository<UtenteAziendaEsterna, Long> {

    public List<UtenteAziendaEsterna> findByUtenteId(long utenteId);
    public List<UtenteAziendaEsterna> findByAziendaId(long aziendaId);

}
