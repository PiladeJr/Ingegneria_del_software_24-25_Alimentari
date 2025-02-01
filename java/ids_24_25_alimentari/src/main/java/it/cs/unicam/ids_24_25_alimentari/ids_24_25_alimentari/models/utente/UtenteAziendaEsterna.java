package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Utente Azienda Esterna")
@NoArgsConstructor
public class UtenteAziendaEsterna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "ID Azienda Produttrice")
    private long idAziendaProduttrice;

    @Column(name = "ID Utente")
    private long idUtente;

    public UtenteAziendaEsterna(long idAzienda, long idUtente) {
        this.idAziendaProduttrice = idAzienda;
        this.idUtente = idUtente;
    }

    public long getId() {
        return id;
    }

    public long getIdAziendaProduttrice() {
        return idAziendaProduttrice;
    }

    public void setIdAziendaProduttrice(long idAziendaProduttrice) {
        this.idAziendaProduttrice = idAziendaProduttrice;
    }

    public long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(long idUtente) {
        this.idUtente = idUtente;
    }
}
