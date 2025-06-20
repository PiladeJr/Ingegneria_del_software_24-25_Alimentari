package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.PacchettoBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.ProdottoBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Pacchetto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Prodotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.ProdottoSingolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.TipoProdotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.PacchettoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.ProdottoSingoloRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProdottoService {

    private final ProdottoSingoloRepository prodottoSingoloRepository;

    private final PacchettoRepository pacchettoRepository;

    public ProdottoService(ProdottoSingoloRepository prodottoRepository, PacchettoRepository pacchettoRepository) {
        this.prodottoSingoloRepository = prodottoRepository;
        this.pacchettoRepository = pacchettoRepository;
    }

    private ProdottoSingolo salvaProdotto(ProdottoSingolo prodotto) {
        return prodottoSingoloRepository.save(prodotto);
    }

    private Pacchetto salvaPacchetto(Pacchetto pacchetto) {
        return pacchettoRepository.save(pacchetto);
    }

    public ProdottoSingolo nuovoProdotto(
            String nome,
            String descrizione,
            Long idAzienda,
            File[] immagini,
            double prezzo,
            int quantita,
            String allergeni,
            String tecniche) {
        ProdottoBuilder builder = new ProdottoBuilder();
        builder.costruisciNome(nome);
        builder.costruisciDescrizione(descrizione);
        builder.costruisciIdAzienda(idAzienda);
        if (immagini != null) {
            for (File immagine : immagini) {
                builder.aggiungiImmagine(immagine);
            }
        }
        builder.costruisciPrezzo(prezzo);
        builder.costruisciQuantita(quantita);
        builder.costruisciAllergeni(allergeni);
        builder.costruisciTecniche(tecniche);

        return salvaProdotto(builder.getProdotto());
    }

    public Pacchetto nuovoPacchetto(
            String nome,
            String descrizione,
            Double prezzo,
            Set<Long> prodotti) {
        PacchettoBuilder builder = new PacchettoBuilder();
        builder.costruisciNome(nome);
        builder.costruisciDescrizione(descrizione);
        builder.costruisciPrezzo(prezzo);
        if (prodotti != null) {
            for (Long prodottoId : prodotti) {
                ProdottoSingolo prod = this.prodottoSingoloRepository.getReferenceById(prodottoId);
                builder.aggiungiProdotto(prod);
            }
        }

        return salvaPacchetto(builder.getPacchetto());
    }

    public Optional<? extends Prodotto> getProdottoById(Long id, TipoProdotto tipo) {
        return switch (tipo) {
            case PACCHETTO -> this.pacchettoRepository.findById(id);
            case SINGOLO -> this.prodottoSingoloRepository.findById(id);
        };
    }

    public Optional<? extends Prodotto> getProdottoByIdWithoutType(Long id) {
        Optional<? extends Prodotto> prodotto = this.pacchettoRepository.findById(id);
        if (prodotto.isEmpty()) {
            prodotto = this.prodottoSingoloRepository.findById(id);
        }
        return prodotto;
    }

    public List<Optional<Prodotto>> getProdottoByNome(String nome) {
        List<Optional<Prodotto>> prod = new ArrayList<>();
        prod.addAll(prodottoSingoloRepository.getProdottoByNome(nome));
        prod.addAll(pacchettoRepository.getProdottoByNome(nome));
        return prod;
    }

    public List<ProdottoSingolo> getProdottiByIdAzienda(Long idAzienda) {
        return this.prodottoSingoloRepository.getProdottiByIdAzienda(idAzienda);
    }

    public List<Prodotto> getAllProdotti(String sortBy, String order) {
        List<Prodotto> prod = new ArrayList<>();
        prod.addAll(prodottoSingoloRepository.findAll());
        prod.addAll(pacchettoRepository.findAll());

        Comparator<Prodotto> comparator;

        switch (sortBy.toLowerCase()) {
            case "prezzo" -> comparator = Comparator.comparing(Prodotto::getPrezzo);
            default -> comparator = Comparator.comparing(Prodotto::getNome, String.CASE_INSENSITIVE_ORDER);
        }

        return prod.stream()
                .sorted(Objects.equals(order, "asc") ? comparator : comparator.reversed())
                .collect(Collectors.toList());
    }

    public void deleteProdotto(Long id, TipoProdotto tipo) {
        switch (tipo) {
            case PACCHETTO -> this.pacchettoRepository.deleteById(id);
            case SINGOLO -> this.prodottoSingoloRepository.deleteById(id);
        }
    }
}
