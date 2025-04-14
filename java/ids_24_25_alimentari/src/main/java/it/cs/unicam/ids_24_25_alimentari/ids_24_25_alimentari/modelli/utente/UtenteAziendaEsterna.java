package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

/**
 * Classe rappresentante la tabella di associazione tra
 * Utente e Azienda
 * (utilizzata per il collegamento tra aziende trasformatore e
 * aziende produttore)
 */
@Entity
@Table(name = "utente_azienda_esterna")
@NoArgsConstructor
public class UtenteAziendaEsterna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long aziendaId;
    private long utenteId;

    public long getId() { return id; }

    public long getAziendaId() { return aziendaId; }

    public void setAziendaId(long aziendaProduttrice) {
        this.aziendaId = aziendaProduttrice;
    }

    public long getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(long utente) {
        this.utenteId = utente;
    }
}
