package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders.BuilderAzienda;
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

    public List<Azienda> getAllAziende(){
        return aziendaRepository.findAll();
    }

    public Optional<Azienda> getAziendaById(Long id){
        return aziendaRepository.findById(id);
    }

    public Azienda saveAzienda(Azienda azienda) {
        return aziendaRepository.save(azienda);
    }

    public void deleteAzienda(Long id) {
        aziendaRepository.deleteById(id);
    }

    public Azienda createAzienda(
            String denSociale,
            Indirizzo sedeLegale,
            Indirizzo sedeOperativa,
            String iva,
            String iban,
            File certificato
            ) {
        BuilderAzienda builder = new BuilderAzienda(new Azienda());
        builder.costruisciDenSociale(denSociale);
        builder.costruisciSedeLegale(sedeLegale);
        builder.costruisciSedeOperativa(sedeOperativa);
        builder.costruisciIva(iva);
        builder.costruisciIban(iban);
        builder.aggiungiCertificato(certificato);
        return saveAzienda(builder.getAzienda());
    }
}
