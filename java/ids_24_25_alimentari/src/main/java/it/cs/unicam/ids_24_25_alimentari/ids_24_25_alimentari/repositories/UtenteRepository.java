package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.Utente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    public Utente findById(long id);

    public Utente findByNome(String nome);

    public Utente findByEmail(String email);

    public Utente findByRuolo(Ruolo ruolo);
}
