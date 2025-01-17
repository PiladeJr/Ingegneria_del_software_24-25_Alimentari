package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Repository;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Entity.Indirizzo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndirizzoRepository extends JpaRepository<Indirizzo,Long> {
    public Indirizzo getIndirizzoById(long id);

}
