package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.transazione.TransazioneDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.transazione.Transazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.TransazioneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransazioneService {
    @Autowired
    private TransazioneRepository transazioneRepository;

    public List<Transazione> getAllTransazioni() {
        return transazioneRepository.findAll();
    }

    public Optional<Transazione> getTransazioneById(Long id) {
        return transazioneRepository.findById(id);
    }

    @Transactional
    public Transazione createTransazione(TransazioneDTO dto) {

        Transazione t = new Transazione();
        t.setImporto(dto.getImporto());
        t.setMetodoPagamento(dto.getMetodoPagamento());
        t.setStatoTransazione(dto.getStatoTransazione());
        t.setPaypalPaymentId(dto.getPaypalPaymentId());
        t.setDataTransazione(java.time.LocalDateTime.now());
        return transazioneRepository.save(t);
    }

    @Transactional
    public Optional<Transazione> updateTransazione(Long id, TransazioneDTO dto) {
        return transazioneRepository.findById(id).map(t -> {

            t.setImporto(dto.getImporto());
            t.setMetodoPagamento(dto.getMetodoPagamento());
            t.setStatoTransazione(dto.getStatoTransazione());
            t.setPaypalPaymentId(dto.getPaypalPaymentId());
            return transazioneRepository.save(t);
        });
    }

    @Transactional
    public boolean deleteTransazione(Long id) {
        if (transazioneRepository.existsById(id)) {
            transazioneRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
