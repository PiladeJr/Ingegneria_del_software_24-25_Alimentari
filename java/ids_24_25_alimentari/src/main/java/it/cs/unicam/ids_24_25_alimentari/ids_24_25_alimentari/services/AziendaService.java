package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders.AziendaBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.UtenteAziendaEsterna;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.AziendaRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.UtenteAziendaEsternaRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class AziendaService {

    private final AziendaRepository aziendaRepository;
    private final UtenteAziendaEsternaRepository utenteAziendaEsternaRepository;

    public AziendaService(AziendaRepository aziendaRepository, UtenteAziendaEsternaRepository utenteAziendaEsternaRepository) {
        this.aziendaRepository = aziendaRepository;
        this.utenteAziendaEsternaRepository = utenteAziendaEsternaRepository;
    }

    public List<Azienda> getAllAziende() {
        return aziendaRepository.findAll();
    }

    public List<Azienda> getAziendeByRuolo(Ruolo ruolo) { return aziendaRepository.findAziendeByRuolo(ruolo); }

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
        AziendaBuilder builder = new AziendaBuilder();
        builder.costruisciDenSociale(denSociale);
        builder.costruisciSedeLegale(sedeLegale);
        builder.costruisciSedeOperativa(sedeOperativa);
        builder.costruisciIva(iva);
        builder.costruisciIban(iban);
        builder.aggiungiCertificato(certificato);
        return saveAzienda(builder.getAzienda());
    }

    /**
     * Collega un utente con ruolo di trasformatore a un'azienda produttrice.
     *
     * @param idUtente             ID dell'utente con ruolo di trasformatore.
     * @param idAziendaProduttrice ID dell'azienda produttrice da collegare.
     * @return l'associazione salvata  nel database.
     * @throws IllegalArgumentException Se l'azienda produttrice non viene trovata.
     */
    public UtenteAziendaEsterna CollegaAzienda(Long idUtente, Long idAziendaProduttrice) {
        UtenteAziendaEsterna collegamento = new UtenteAziendaEsterna();
        Azienda azienda = aziendaRepository.findAziendaByIdAndruolo(idAziendaProduttrice, Ruolo.PRODUTTORE);

        if (azienda == null) {
            throw new IllegalArgumentException("Azienda non trovata");
        }

        collegamento.setUtenteId(idUtente);
        collegamento.setAziendaId(azienda.getId());

        return utenteAziendaEsternaRepository.save(collegamento);
    }

}
