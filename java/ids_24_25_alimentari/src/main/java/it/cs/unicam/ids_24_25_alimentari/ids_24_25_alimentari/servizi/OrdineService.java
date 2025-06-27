package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.ordine.OrdineDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.transazione.TransazioneDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.Ordine;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine.StatoOrdine;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.transazione.StatoTransazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.OrdineRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdineService {

    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private IndirizzoService indirizzoService;

    @Autowired
    private CarrelloService carrelloService;

    @Autowired
    private TransazioneService transazioneService;

    public List<Ordine> getAllOrdini() {
        return ordineRepository.findAll();
    }

    public Optional<Ordine> getOrdineById(Long id) {
        return ordineRepository.findById(id);
    }

    public Ordine salvaOrdine(OrdineDTO ordine) {
        var carrello = carrelloService.getCarrelloById(ordine.getIdCarrello())
                .orElseThrow(() -> new RuntimeException("Carrello non trovato"));
        // Recupera gli indirizzi di spedizione e fatturazione
        var indirizzoSpedizione = indirizzoService.findById(carrello.getUtente().getIndirizzoSpedizione().getId());
        var indirizzoFatturazione = indirizzoService.findById(carrello.getUtente().getIndirizzoFatturazione().getId());

        // Crea l'oggetto Ordine dal DTO
        Ordine nuovoOrdine = new Ordine();
        nuovoOrdine.setIndirizzoConsegna(
                indirizzoSpedizione.orElseThrow(() -> new RuntimeException("Indirizzo di spedizione non trovato")));
        nuovoOrdine.setIndirizzoFatturazione(
                indirizzoFatturazione.orElseThrow(() -> new RuntimeException("Indirizzo di fatturazione non trovato")));

        nuovoOrdine.setStato(StatoOrdine.IN_ATTESA);
        nuovoOrdine.setCarrello(carrello);
        nuovoOrdine.setUtente(carrello.getUtente());
        nuovoOrdine.setDataOrdine(java.time.LocalDateTime.now());
        nuovoOrdine.setTotale(carrello.getContenutoCarrello().stream()
                .mapToDouble(prodotto -> prodotto.getQuantita() * prodotto.getProdotto().getPrezzo())
                .sum());

        // Crea la transazione in attesa
        TransazioneDTO transazioneDTO = new TransazioneDTO();
        transazioneDTO.setImporto(nuovoOrdine.getTotale());
        transazioneDTO.setMetodoPagamento(ordine.getMetodoPagamento());
        transazioneDTO.setStatoTransazione(StatoTransazione.IN_ATTESA);
        var transazione = transazioneService.createTransazione(transazioneDTO);

        nuovoOrdine.setTransazione(transazioneService.getTransazioneById(
                transazione.getId())
                .orElseThrow(() -> new RuntimeException("Transazione non trovata")));

        Ordine ordineSalvato = ordineRepository.save(nuovoOrdine);

        return ordineSalvato;

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
