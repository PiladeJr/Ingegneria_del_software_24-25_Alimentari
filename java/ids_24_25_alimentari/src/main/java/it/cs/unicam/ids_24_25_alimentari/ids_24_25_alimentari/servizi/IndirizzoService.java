package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.IndirizzoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class IndirizzoService {

    @Autowired
    private IndirizzoRepository indirizzoRepository;

    public List<Indirizzo> findAll() {
        return indirizzoRepository.findAll();
    }

    public Optional<Indirizzo> findById(Long id) {
        return indirizzoRepository.findById(id);
    }

    public Indirizzo save(Indirizzo indirizzo) {
        return indirizzoRepository.save(indirizzo);
    }

    public void deleteById(Long id) {
        indirizzoRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return indirizzoRepository.existsById(id);
    }
}
