package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {

    @Query("SELECT o FROM Ordine o WHERE o.transazione.paypalPaymentId = :paypalPaymentId")
    Optional<Ordine> findByPaypalPaymentId(@Param("paypalPaymentId") String paypalPaymentId);
}
