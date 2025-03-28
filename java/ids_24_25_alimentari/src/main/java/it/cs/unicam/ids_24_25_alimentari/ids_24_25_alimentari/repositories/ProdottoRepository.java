package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.prodotto.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    @Query("SELECT p FROM Prodotto p WHERE p.idAzienda = ?1")
    List<Prodotto> getProdottiByIdAzienda(Long idAzienda);
    @Query("SELECT p FROM Prodotto p WHERE p.nome = ?1")
    Prodotto getProdottoByNome(String nome);
}