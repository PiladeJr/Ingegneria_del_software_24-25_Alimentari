package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.carrello.Carrello;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.carrello.ContenutoCarrello;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.CarrelloRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.ContenutoCarrelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarrelloService {

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private ContenutoCarrelloRepository contenutoCarrelloRepository;

    public List<Carrello> getAllCarrelli() {
        return carrelloRepository.findAll();
    }

    public Optional<Carrello> getCarrelloById(Long id) {
        return carrelloRepository.findById(id);
    }

    public Carrello salvaCarrello(Carrello carrello) {
        return carrelloRepository.save(carrello);
    }

    public void eliminaCarrello(Long id) {
        carrelloRepository.deleteById(id);
    }

    public Carrello aggiungiContenutoAlCarrello(Long carrelloId, ContenutoCarrello contenutoCarrello) {
        Carrello carrello = carrelloRepository.findById(carrelloId)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato"));

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
