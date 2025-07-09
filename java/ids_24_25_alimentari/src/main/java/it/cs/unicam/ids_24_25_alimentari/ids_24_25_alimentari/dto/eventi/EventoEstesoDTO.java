package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.indirizzo.IndirizzoDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.Evento;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi.StatusEvento;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.LocalDateTime;

@Getter
@Setter
public class EventoEstesoDTO {
    private long id;
    private String titolo;
    private String descrizione;
    private StatusEvento status;
    private LocalDateTime inizio;
    private LocalDateTime fine;
    private File locandina;
    private IndirizzoDTO indirizzo;
    private String creatore;

    public EventoEstesoDTO(Evento evento) {
        this.id = evento.getId();
        this.titolo = evento.getTitolo();
        this.descrizione = evento.getDescrizione();
        this.status = evento.getStatus();
        this.inizio = evento.getInizio();
        this.fine = evento.getFine();
        this.locandina = evento.getLocandina();
        this.indirizzo = new IndirizzoDTO(evento.getIndirizzo());
        this.creatore = evento.getCreatore().getEmail();
    }
}
