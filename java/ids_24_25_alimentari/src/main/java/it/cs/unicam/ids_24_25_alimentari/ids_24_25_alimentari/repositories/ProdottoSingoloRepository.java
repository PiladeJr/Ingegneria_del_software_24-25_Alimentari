package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.contenuto.prodotto.ProdottoSingolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdottoSingoloRepository extends JpaRepository<ProdottoSingolo, Long> {
    @Query("SELECT p FROM ProdottoSingolo p WHERE p.idAzienda = ?1")
    List<ProdottoSingolo> getProdottiByIdAzienda(Long idAzienda);
    @Query("SELECT p FROM ProdottoSingolo p WHERE p.nome = ?1")
    ProdottoSingolo getProdottoByNome(String nome);
}