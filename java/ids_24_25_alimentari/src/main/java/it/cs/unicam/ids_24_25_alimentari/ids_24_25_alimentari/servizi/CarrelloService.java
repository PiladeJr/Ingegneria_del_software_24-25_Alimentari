package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.carrello.CreaCarrelloDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.contenutoCarrello.ContenutoCarrelloDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.carrello.Carrello;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.carrello.ContenutoCarrello;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.Prodotto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.CarrelloRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.ContenutoCarrelloRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.ProdottoService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        return carrelloRepository.findById(id);
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
        ContenutoCarrello contenutoCarrello = contenutoCarrelloRepository.findById(contenutoCarrelloId)
                .orElseThrow(() -> new RuntimeException("Contenuto carrello non trovato"));

        carrello.getContenutoCarrello().remove(contenutoCarrello);
        contenutoCarrelloRepository.delete(contenutoCarrello);
        return carrelloRepository.save(carrello);
    }
}
