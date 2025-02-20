package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.AziendaBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.AziendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class AziendaService {

    @Autowired
    private AziendaRepository aziendaRepository;
    private AziendaBuilder builder = new AziendaBuilder();

    public List<Azienda> getAllAziende() {
        return aziendaRepository.findAll();
    }

    public List<Azienda> getAziendeByRuolo(Ruolo ruolo) {
        List<Azienda> aziendeProduttori = aziendaRepository.findAziendeByRuolo(ruolo);
        return aziendeProduttori;
    }

    public Optional<Azienda> getAziendaById(long id) {
        return aziendaRepository.findById(id);
    }

    public Azienda saveAzienda(Azienda azienda) {
        return aziendaRepository.save(azienda);
    }

    public void deleteAzienda(long id) {
        aziendaRepository.deleteById(id);
    }

    public Azienda createAzienda(
            String denSociale,
            Indirizzo sedeLegale,
            Indirizzo sedeOperativa,
            String iva,
            String iban,
            File certificato) {
        builder.costruisciDenSociale(denSociale);
        builder.costruisciSedeLegale(sedeLegale);
        builder.costruisciSedeOperativa(sedeOperativa);
        builder.costruisciIva(iva);
        builder.costruisciIban(iban);
        builder.aggiungiCertificato(certificato);
        return saveAzienda(builder.getAzienda());
    }

}
