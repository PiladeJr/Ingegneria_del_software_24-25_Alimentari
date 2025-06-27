package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.UtenteService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp.ImplementazioneServizioMail;
import org.springframework.stereotype.Service;

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

        List<String> gestori = utenteService.getEmailsByRuolo(ruolo);
        if (gestori == null && gestori.isEmpty()) {
            throw new RuntimeException("Nessun curatore trovato nel database");
        }
        return gestori;

    }

    private void notificaUtenti(String oggetto, String messaggio, List<String> destinatari) {
        for (String mail : destinatari) {
            if (mail != null && !mail.isEmpty()) {
                mailService.inviaMail(oggetto, messaggio, mail);
            }
        }
    }

    public void notificaNuovaRichiesta(Ruolo ruolo) {
        String messaggio = "Una nuova richiesta in attesa di valutazione è stata salvata nel database.";
        String oggetto = "Notifica Nuova Richiesta";
        notificaUtenti(oggetto, messaggio, fornisciDestinatari(ruolo));
    }

}
