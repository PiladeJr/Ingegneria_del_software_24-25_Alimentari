package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.*;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaCollaborazioneRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders.BuilderRichiestaCollaborazione;
import java.io.File;

@Service
public class RichiesteCollaborazioneService {
    @Autowired
    private RichiestaCollaborazioneRepository richiestaCollaborazioneRepository;
    @Autowired
    private AziendaService aziendaService;
    @Autowired
    private final UtenteService utenteService;

    public RichiesteCollaborazioneService(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    public List<RichiestaCollaborazione> getAllRichieste() {
        return richiestaCollaborazioneRepository.findAll();
    }

    public Optional<RichiestaCollaborazione> getRichiestaById(Long id) {
        return richiestaCollaborazioneRepository.findById(id);
    }

    public RichiestaCollaborazione saveRichiesta(RichiestaCollaborazione richiesta) {
        return richiestaCollaborazioneRepository.save(richiesta);
    }

    public void deleteRichiesta(Long id) {
        richiestaCollaborazioneRepository.deleteById(id);
    }

    public RichiestaCollaborazione creaRichiestaAzienda(
            String nome,
            String cognome,
            String telefono,
            String email,
            Ruolo ruolo,
            String denSociale,
            Indirizzo sedeLegale,
            Indirizzo sedeOperativa,
            String iban,
            String iva,
            File certificato,
            File cartaIdentita) {
        BuilderRichiestaCollaborazione builder = new BuilderRichiestaCollaborazione();
        RichiestaCollaborazioneDirector director = new RichiestaCollaborazioneDirector(builder);
        director.creaAzienda(nome, cognome, telefono, email, ruolo, denSociale, sedeLegale, sedeOperativa, iban, iva,
                certificato, cartaIdentita);
        return saveRichiesta(builder.getRichiesta());
    }

    public RichiestaCollaborazione creaRichiestaAnimatore(
            String nome,
            String cognome,
            String telefono,
            String email,
            Ruolo ruolo,
            String aziendaReferente,
            String iban,
            File cartaIdentita) {
        BuilderRichiestaCollaborazione builder = new BuilderRichiestaCollaborazione();
        RichiestaCollaborazioneDirector director = new RichiestaCollaborazioneDirector(builder);
        director.creaAnimatore(nome, cognome, telefono, email, ruolo, aziendaReferente, iban, cartaIdentita);
        return saveRichiesta(builder.getRichiesta());
    }

    public RichiestaCollaborazione creaRichiestaCuratore(
            String nome,
            String cognome,
            String telefono,
            String email,
            Ruolo ruolo,
            String iban,
            File cartaIdentita,
            File cv) {
        BuilderRichiestaCollaborazione builder = new BuilderRichiestaCollaborazione();
        RichiestaCollaborazioneDirector director = new RichiestaCollaborazioneDirector(builder);
        director.creaCuratore(nome, cognome, telefono, email, ruolo, iban, cartaIdentita, cv);
        return saveRichiesta(builder.getRichiesta());
    }

    public RichiestaCollaborazione setStato(Long id, Boolean stato) {
        Optional<RichiestaCollaborazione> richiesta = getRichiestaById(id);
        if (richiesta.isPresent()) {
            richiesta.get().setStato(stato);
            if(stato) {
                generaAccount(id);
            }
            return saveRichiesta(richiesta.get());
        }
        return null;
    }

    public void generaAccount(Long id) {
        Optional<RichiestaCollaborazione> richiesta = getRichiestaById(id);
        if (richiesta.isPresent()) {
            RichiestaCollaborazione richiestaCollaborazione = richiesta.get();
            switch(richiestaCollaborazione.getRuolo()){
            case PRODUTTORE, TRASFORMATORE, DISTRIBUTORE -> {
                Azienda azienda = aziendaService.createAzienda(
                    richiestaCollaborazione.getDenominazioneSociale(),
                    richiestaCollaborazione.getSedeLegale(),
                    richiestaCollaborazione.getSedeOperativa(),
                    richiestaCollaborazione.getIva(),
                    richiestaCollaborazione.getIban(),
                    richiestaCollaborazione.getCertificato()
                    );

                utenteService.nuovoAzienda(
                        richiestaCollaborazione.getNome(),
                        richiestaCollaborazione.getCognome(),
                        richiestaCollaborazione.getEmail(),
                        richiestaCollaborazione.getTelefono(),
                        richiestaCollaborazione.getRuolo(),
                        azienda.getId(),
                        richiestaCollaborazione.getCartaIdentita()
                        );
                }
            }

        }
    }
}
