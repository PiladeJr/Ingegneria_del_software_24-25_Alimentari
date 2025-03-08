package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders.ProdottoBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.prodotto.Prodotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.ProdottoRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ProdottoService {

    private final ProdottoRepository prodottoRepository;

    public ProdottoService(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
    }

    public Prodotto salvaProdotto(Prodotto prodotto) {
        return prodottoRepository.save(prodotto);
    }

    public Prodotto nuovoProdotto(
            String nome,
            String descrizione,
            Long idAzienda,
            File[] immagini,
            double prezzo,
            int quantita,
            String allergeni,
            String tecniche
    ) {
        ProdottoBuilder builder = new ProdottoBuilder();
        builder.costruisciNome(nome);
        builder.costruisciDescrizione(descrizione);
        builder.costruisciIdAzienda(idAzienda);
        if(immagini != null) {
            for(File immagine : immagini) {
                builder.aggiungiImmagine(immagine);
            }
        }
        builder.costruisciPrezzo(prezzo);
        builder.costruisciQuantita(quantita);
        builder.costruisciAllergeni(allergeni);
        builder.costruisciTecniche(tecniche);

        return salvaProdotto(builder.getProdotto());
    }

    public Prodotto getProdottoByNome(String nome) {
        return this.prodottoRepository.getProdottoByNome(nome);
    }

    public List<Prodotto> getProdottiByIdAzienda(Long idAzienda) {
        return this.prodottoRepository.getProdottiByIdAzienda(idAzienda);
    }



}
