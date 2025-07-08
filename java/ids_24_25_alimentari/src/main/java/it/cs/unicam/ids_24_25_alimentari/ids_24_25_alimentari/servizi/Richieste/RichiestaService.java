package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.ValutaRichiestaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione.RichiestaCollaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.UtenteService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp.ImplementazioneServizioMail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public abstract class RichiestaService implements RichiestaInterface {

    private final ImplementazioneServizioMail mailService;
    protected final UtenteService utenteService;

    public RichiestaService(ImplementazioneServizioMail mailService, UtenteService utenteService) {
        this.mailService = mailService;
        this.utenteService = utenteService;
    }

    public List<String> fornisciDestinatari(Ruolo ruolo) {

        ArrayList<String> admin = (ArrayList<String>) utenteService.getEmailsByRuolo(ruolo);
        if (admin == null && admin.isEmpty()) {
            throw new RuntimeException("Nessun curatore trovato nel database");
        }
        return admin;
    }

    private void notificaUtenti(String oggetto, String messaggio, List<String> destinatari) {
        for (String mail : destinatari) {
            notificaUtenti(oggetto, messaggio, mail);
        }
    }

    private void notificaUtenti(String oggetto, String messaggio, String destinatario) {
        if (destinatario != null && !destinatario.isEmpty()) {
            mailService.inviaMail(oggetto, messaggio, destinatario);
        }
    }

    public void notificaNuovaRichiesta(Ruolo ruolo) {
        String messaggio = "Una nuova richiesta in attesa di valutazione è stata salvata nel database.";
        String oggetto = "Notifica Nuova Richiesta";
        notificaUtenti(oggetto, messaggio, fornisciDestinatari(ruolo));
    }

    public void notificaAccettazioneRichiesta(String messaggio, String email, String tipologia) {
        String oggetto ="Elaborazione Richiesta di " + tipologia;
        notificaUtenti(oggetto, messaggio,email);
    }
    public void notificaRifiutoRichiesta(String rifiuto, String email, String tipologia) {
        String messaggio = "La sua richiesta di " + tipologia + " è stata rifiutata per la seguente motivazione: " +
                (rifiuto);
        String oggetto ="Elaborazione Richiesta di Collaborazione";
        notificaUtenti(oggetto, messaggio,email);
    }


}
