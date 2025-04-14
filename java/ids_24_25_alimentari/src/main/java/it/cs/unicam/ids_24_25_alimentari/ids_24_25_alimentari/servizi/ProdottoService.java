package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.PacchettoBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.ProdottoBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Pacchetto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.ProdottoSingolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.PacchettoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.ProdottoSingoloRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Set;

@Service
public class ProdottoService {

    private final ProdottoSingoloRepository prodottoSingoloRepository;

    private final PacchettoRepository pacchettoRepository;

    public ProdottoService(ProdottoSingoloRepository prodottoRepository, PacchettoRepository pacchettoRepository) {
        this.prodottoSingoloRepository = prodottoRepository;
        this.pacchettoRepository = pacchettoRepository;
    }

    public ProdottoSingolo salvaProdotto(ProdottoSingolo prodotto) {
        return prodottoSingoloRepository.save(prodotto);
    }

    private Pacchetto salvaPacchetto(Pacchetto pacchetto) { return pacchettoRepository.save(pacchetto); }

    public ProdottoSingolo nuovoProdotto(
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

    public Pacchetto nuovoPacchetto(
            String nome,
            String descrizione,
            Double prezzo,
            Set<Long> prodotti
    ) {
        PacchettoBuilder builder = new PacchettoBuilder();
        builder.costruisciNome(nome);
        builder.costruisciDescrizione(descrizione);
        builder.costruisciPrezzo(prezzo);
        if(prodotti != null) {
            for(Long prodottoId : prodotti) {
                ProdottoSingolo prod = this.prodottoSingoloRepository.getReferenceById(prodottoId);
                builder.aggiungiProdotto(prod);
            }
        }

        return salvaPacchetto(builder.getPacchetto());
    }

    public ProdottoSingolo getProdottoByNome(String nome) {
        return this.prodottoSingoloRepository.getProdottoByNome(nome);
    }

    public List<ProdottoSingolo> getProdottiByIdAzienda(Long idAzienda) {
        return this.prodottoSingoloRepository.getProdottiByIdAzienda(idAzienda);
    }

}
