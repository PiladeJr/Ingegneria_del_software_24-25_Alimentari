package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.PacchettoBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.ProdottoBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Pacchetto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Prodotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.ProdottoSingolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.TipoProdotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.PacchettoRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.ProdottoSingoloRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.EnumComuni.Status;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProdottoService {

    private final ProdottoSingoloRepository prodottoSingoloRepository;
    private final PacchettoRepository pacchettoRepository;
    private final UtenteService utenteService;
    private final AziendaService aziendaService;

    public ProdottoService(ProdottoSingoloRepository prodottoRepository, PacchettoRepository pacchettoRepository, UtenteService utenteService, AziendaService aziendaService) {
        this.prodottoSingoloRepository = prodottoRepository;
        this.pacchettoRepository = pacchettoRepository;
        this.utenteService = utenteService;
        this.aziendaService = aziendaService;
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
        ProdottoSingolo prodotto = builder.getProdotto();
        prodotto.setStatus(Status.PENDING);
        prodotto.setVersione(1);
        return salvaProdotto(prodotto);
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
        Pacchetto pacchetto = builder.getPacchetto();
        pacchetto.setStatus(Status.PENDING);
        pacchetto.setVersione(1);
        return salvaPacchetto(pacchetto);
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

    /**
     * <h2>Rimuove un prodotto dallo shop</h2>
     * <br>
     * Questo metodo consente di rimuovere un prodotto singolo dallo shop.
     * Verifica che il prodotto esista, che sia approvato e che appartenga
     * all'azienda dell'utente autenticato. Inoltre, controlla se il prodotto
     * è presente in un pacchetto e, in tal caso, rimuove anche il pacchetto dallo shop.
     *
     * @param id L'ID del prodotto da rimuovere.
     * @throws NoSuchElementException Se il prodotto non esiste o l'azienda non è trovata.
     * @throws IllegalArgumentException Se il prodotto non appartiene all'azienda loggata.
     */
    public void rimuoviProdottoDalloShop(long id) {
        Optional<ProdottoSingolo> prodotto = prodottoSingoloRepository.findById(id);
        if (prodotto.isPresent() && prodotto.get().getStatus() == Status.APPROVATO) {
            ProdottoSingolo prod = prodotto.get();
            Azienda azienda = aziendaService.getAziendaByUtente(utenteService.getIdUtenteAutenticato())
                    .orElseThrow(() -> new NoSuchElementException("Azienda non trovata per l'utente autenticato"));
            if (prod.getIdAzienda().equals(azienda.getId())) {
                // Controlla se il prodotto è presente in un pacchetto
                List<Pacchetto> pacchetti = pacchettoRepository.findPacchettiByProdottoId(prod.getId());
                for (Pacchetto pacchetto : pacchetti) {
                    if (pacchetto.getProdotti().contains(prod)) {
                        rimuoviPacchettoDalloShop(pacchetto.getId());
                    }
                }
                prod.setStatus(Status.ELIMINATO);
                prodottoSingoloRepository.save(prod);
            } else {
                throw new IllegalArgumentException("Il prodotto non appartiene all'azienda loggata.");
            }
        } else {
            throw new NoSuchElementException("Prodotto non trovato.");
        }
    }

    /**
     * <h2>Rimuove un pacchetto dallo shop</h2>
     * <br>
     * Questo metodo consente di rimuovere un pacchetto dallo shop.
     * Verifica che il pacchetto esista e che sia approvato.
     * Se il pacchetto è presente, rimuove il collegamento con i prodotti,
     * imposta lo stato del pacchetto a "ELIMINATO" e lo salva nel repository.
     *
     * @param id L'ID del pacchetto da rimuovere.
     * @throws NoSuchElementException Se il pacchetto non esiste o non è approvato.
     */
    public void rimuoviPacchettoDalloShop(long id) {
        Optional<Pacchetto> pacchettoOpt = pacchettoRepository.findById(id);
        if (pacchettoOpt.isPresent() && pacchettoOpt.get().getStatus() == Status.APPROVATO) {
            if (true) {
                Pacchetto pacchetto = pacchettoOpt.get();

                // Rimuove il collegamento con i prodotti
                pacchetto.getProdotti().clear();

                // Imposta lo status a eliminato
                pacchetto.setStatus(Status.ELIMINATO);

                pacchettoRepository.save(pacchetto);
            }
        } else {
            throw new NoSuchElementException("Pacchetto non trovato o non approvato.");
        }
    }

    public void deleteProdotto(long id){

    }

    public void deletePacchetto(long id){

    }

    public void deleteProdotto(Long id, TipoProdotto tipo) {
        switch (tipo) {
            case PACCHETTO -> this.pacchettoRepository.deleteById(id);
            case SINGOLO -> this.prodottoSingoloRepository.deleteById(id);
        }
    }
}
