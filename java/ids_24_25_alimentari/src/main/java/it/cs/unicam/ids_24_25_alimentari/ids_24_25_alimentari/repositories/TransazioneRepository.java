package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.transazione.Transazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransazioneRepository extends JpaRepository<Transazione, Long> {

    Optional<Transazione> findByPaypalPaymentId(String paypalPaymentId);
}
