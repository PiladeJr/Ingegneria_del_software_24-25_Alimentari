package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.RichiestaCollaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.smtp.ServizioEmail;
import org.springframework.stereotype.Service;

@Service
public class GestionePermessiService {
    private final RichiesteCollaborazioneService richiestaCollaborazioneService;
    private final ServizioEmail servizioEmail;

    public GestionePermessiService(RichiesteCollaborazioneService richiestaCollaborazioneService, ServizioEmail servizioEmail) {
        this.richiestaCollaborazioneService = richiestaCollaborazioneService;
        this.servizioEmail = servizioEmail;
    }

    /**
     * Gestore della piattaforma accetta la richiesta di collaborazione dell'utente,
     * inviandogli una mail di conferma
     *
     * @param idRichiesta
     *
     */
    public RichiestaCollaborazione AccettaRichiestaCollaborazione(long idRichiesta){
        this.richiestaCollaborazioneService.generaAccount(idRichiesta);
        RichiestaCollaborazione richiesta = this.richiestaCollaborazioneService.getRichiestaById(idRichiesta).orElseThrow(()->new IllegalStateException("richiesta non trovata"));
        System.out.println(richiesta.getId());
        String messaggio = "La richiesta di collaborazione accettata con successo!";
        this.servizioEmail.inviaMail(richiesta.getEmail(), messaggio, "Accettazione Richiesta di Collaborazione");

        return richiesta;
    }

    /**
     * Gestore della piattaforma rifiuta la richiesta di collaborazione dell'utente,
     * inviandogli una mail contenente il motivo del rifiuto
     *
     * @param idRichiesta l'id della richiesta che si intende rifiutare
     * @param messaggioRifiuto la motivazione del rifiuto da inviare all'utente
     *
     */
    public void RifiutaRichiestaCollaborazione(long idRichiesta, String messaggioRifiuto){
        RichiestaCollaborazione richiesta = this.richiestaCollaborazioneService.getRichiestaById(idRichiesta).get();

        String messaggio = "La richiesta è stata rifiutata per il seguente motivo" + messaggioRifiuto;

        this.servizioEmail.inviaMail(richiesta.getEmail(), messaggio, "Accettazione Richiesta di Collaborazione");
    }


}
