package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.AziendaBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.AziendaRepository;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class AziendaService {

    private final AziendaRepository aziendaRepository;
    private final UtenteService utenteService;

    public AziendaService(AziendaRepository aziendaRepository, UtenteService utenteService) {
        this.aziendaRepository = aziendaRepository;
        this.utenteService = utenteService;
    }

    public List<Azienda> getAllAziende() {
        return aziendaRepository.findAll();
    }

    public List<Azienda> getAziendeByRuolo(Ruolo ruolo) {
        return aziendaRepository.findAziendeByRuolo(ruolo);
    }

    public Optional<Azienda> getAziendaById(long id) {
        return aziendaRepository.findById(id);
    }

    public Optional<Azienda> getAziendaByUtente(long id) {
        return aziendaRepository.findAziendaByUtenteId(id);
    }

    public Azienda saveAzienda(Azienda azienda) {
        return aziendaRepository.save(azienda);
    }

    public void deleteAzienda(long id) {
        aziendaRepository.deleteById(id);
    }

    /**
     * <h2>Crea una nuova azienda</h2>
     * <br/>
     * <p>Metodo responsabile della creazione di una nuova azienda.</p>
     * <p>Utilizza un builder per costruire l'oggetto azienda con i dati forniti.</p>
     * <p>Restituisce l'azienda salvata nella repository.</p>
     *
     * @param denSociale La denominazione sociale dell'azienda.
     * @param sedeLegale L'indirizzo della sede legale dell'azienda.
     * @param sedeOperativa L'indirizzo della sede operativa dell'azienda.
     * @param iva Il numero di partita IVA dell'azienda.
     * @param certificato Il file contenente il certificato dell'azienda.
     * @param utente L'utente associato all'azienda.
     * @return {@link Azienda} L'azienda salvata nella repository.
     */
    public Azienda createAzienda(
            String denSociale,
            Indirizzo sedeLegale,
            Indirizzo sedeOperativa,
            String iva,
            File certificato,
            Utente utente) {
        AziendaBuilder builder = new AziendaBuilder();
        builder.costruisciDenSociale(denSociale);
        builder.costruisciSedeLegale(sedeLegale);
        builder.costruisciSedeOperativa(sedeOperativa);
        builder.costruisciIva(iva);
        builder.aggiungiCertificato(certificato);
        builder.costruisciUtente(utente);
        return saveAzienda(builder.getAzienda());
    }

    /**
     * <h2>Collega aziende a un utente</h2>
     * <br/>
     * <p>Metodo responsabile dell'associazione di una lista di aziende a un utente specifico.</p>
     * <p>Se l'utente con l'ID fornito non viene trovato, viene lanciata un'eccezione.</p>
     *
     * @param utenteId L'ID dell'utente a cui collegare le aziende.
     * @param aziende La lista di aziende da collegare all'utente.
     * @throws IllegalArgumentException Se l'utente con l'ID fornito non viene trovato.
     */
    public void collegaAziendeUtente(long utenteId, List<Azienda> aziende) {

        Utente utente = utenteService.getUtenteById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + utenteId));
        utente.getAziendeCollegate().addAll(aziende);

        utenteService.salvaUtente(utente);
    }

}
