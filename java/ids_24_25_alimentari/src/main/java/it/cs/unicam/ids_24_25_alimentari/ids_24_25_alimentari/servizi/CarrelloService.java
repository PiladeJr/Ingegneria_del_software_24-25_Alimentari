package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.carrello.CreaCarrelloDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.contenutoCarrello.ContenutoCarrelloDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.carrello.Carrello;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.carrello.ContenutoCarrello;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Prodotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.CarrelloRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.ContenutoCarrelloRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.UtenteRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.EnumComuni.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CarrelloService {

    @Autowired
    private CarrelloRepository carrelloRepository;
    @Autowired
    private ContenutoCarrelloRepository contenutoCarrelloRepository;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private ProdottoService prodottoService;

    public List<Carrello> getAllCarrelli() {
        return carrelloRepository.findAll();
    }

    public Optional<Carrello> getCarrelloById(Long id) {
        Optional<Carrello> carrello = carrelloRepository.findById(id);
        if (carrello.isPresent()) {
            return Optional.of(verificaEsistenzaProdotti(carrello.get()));
        } else
            throw new NoSuchElementException("Carrello non trovato con id: " + id);
    }

    public Carrello getCarrelloByUserId(Long userId) {
        List<Carrello> carrelli = carrelloRepository.findByUtenteId(userId);
        if (carrelli.isEmpty()) {
            throw new NoSuchElementException("Nessun carrello trovato per l'utente con id: " + userId);
        }
        return verificaEsistenzaProdotti(carrelli.get(0));
    }

    public Carrello creaCarrello(CreaCarrelloDTO dto) {
        Utente utente = utenteRepository.findById(dto.getIdUtente())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Set<ContenutoCarrello> contenuti = new HashSet<>();
        for (Long idProdotto : dto.getIdProdotti()) {
            ContenutoCarrello contenuto = contenutoCarrelloRepository.findById(idProdotto)
                    .orElseThrow(() -> new RuntimeException("ContenutoCarrello non trovato: " + idProdotto));
            contenuti.add(contenuto);
        }

        Carrello carrello = new Carrello();
        carrello.setUtente(utente);
        carrello.setContenutoCarrello(contenuti);
        carrello.setData_creazione(LocalDateTime.now());

        return carrelloRepository.save(carrello);
    }

    public void eliminaCarrello(Long id) {
        carrelloRepository.deleteById(id);
    }

    public Carrello aggiungiContenutoAlCarrello(Long carrelloId, ContenutoCarrelloDTO contenutoCarrelloDTO) {
        Carrello carrello = carrelloRepository.findById(carrelloId)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato"));
        carrello = verificaEsistenzaProdotti(carrello);

        // Ottieni il prodotto dall'id

        Prodotto prodotto = prodottoService.getProdottoByIdWithoutType(contenutoCarrelloDTO.getProdottoId())
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        ContenutoCarrello contenutoCarrello = new ContenutoCarrello(
                prodotto,
                contenutoCarrelloDTO.getQuantita());

        contenutoCarrelloRepository.save(contenutoCarrello);
        carrello.getContenutoCarrello().add(contenutoCarrello);
        return carrelloRepository.save(carrello);
    }

    public Carrello rimuoviContenutoDalCarrello(Long carrelloId, Long contenutoCarrelloId) {
        Carrello carrello = carrelloRepository.findById(carrelloId)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato"));
        carrello = verificaEsistenzaProdotti(carrello);
        ContenutoCarrello contenutoCarrello = contenutoCarrelloRepository.findById(contenutoCarrelloId)
                .orElseThrow(() -> new RuntimeException("Contenuto carrello non trovato"));

        carrello.getContenutoCarrello().remove(contenutoCarrello);
        contenutoCarrelloRepository.delete(contenutoCarrello);
        return carrelloRepository.save(carrello);
    }

    private Carrello verificaEsistenzaProdotti(Carrello carrello) {
        Set<ContenutoCarrello> contenutiDaRimuovere = new HashSet<>();

        for (ContenutoCarrello contenuto : carrello.getContenutoCarrello()) {
            Prodotto prodotto = prodottoService.getProdottoByIdWithoutType(contenuto.getProdotto().getId())
                    .orElse(null);

            if (prodotto == null || prodotto.getStatus() == Status.ELIMINATO) {
                contenutiDaRimuovere.add(contenuto);
            }
        }

        for (ContenutoCarrello contenutoDaRimuovere : contenutiDaRimuovere) {
            carrello.getContenutoCarrello().remove(contenutoDaRimuovere);
            contenutoCarrelloRepository.delete(contenutoDaRimuovere);
        }

        return carrelloRepository.save(carrello);
    }
}
