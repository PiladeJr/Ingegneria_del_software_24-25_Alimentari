package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Pacchetto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PacchettoRepository extends JpaRepository<Pacchetto, Long> {
    @Query("SELECT p FROM Pacchetto p WHERE p.nome = ?1")
    List<Optional<Prodotto>> getProdottoByNome(String nome);

    @Query("SELECT p FROM Pacchetto p JOIN p.prodotti prod WHERE prod.id = ?1 AND p.status = 'APPROVATO' AND prod.status = 'APPROVATO'")
    List<Pacchetto> findPacchettiByProdottoId(Long prodottoId);
}
