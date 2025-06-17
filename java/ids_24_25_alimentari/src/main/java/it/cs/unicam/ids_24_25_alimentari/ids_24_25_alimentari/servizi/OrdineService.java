package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.Ordine;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.StatoOrdine;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.OrdineRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdineService {

    @Autowired
    private OrdineRepository ordineRepository;

    public List<Ordine> getAllOrdini() {
        return ordineRepository.findAll();
    }

    public Optional<Ordine> getOrdineById(Long id) {
        return ordineRepository.findById(id);
    }

    public Ordine salvaOrdine(Ordine ordine) {
        return ordineRepository.save(ordine);
    }

    public Ordine aggiornaStatoOrdine(Long id, StatoOrdine nuovoStato) {
        Optional<Ordine> ordineOpt = ordineRepository.findById(id);
        if (ordineOpt.isPresent()) {
            Ordine ordine = ordineOpt.get();
            ordine.setStato(nuovoStato);
            return ordineRepository.save(ordine);
        }
        throw new RuntimeException("Ordine non trovato");
    }
}
