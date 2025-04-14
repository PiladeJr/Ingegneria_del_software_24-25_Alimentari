package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "richiesta")
@NoArgsConstructor
public class Richiesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Tipologia tipologia;

    @Column(nullable = true)
    private Boolean approvato;

    @Column(nullable = false)
    private long idMittente;
    private long idInformazioni;
    private long idCuratore;

    public long getId() {
        return id;
    }

    public void setApprovazione(boolean approvato) {
        this.approvato = approvato;
    }

    // Getters and setters for the other fields can be added as needed
    public Tipologia getTipologia() {
        return tipologia;
    }

    public void setTipologia(Tipologia tipologia) {
        this.tipologia = tipologia;
    }

    public Boolean isApprovato() {
        return approvato;
    }

    public long getIdMittente() {
        return idMittente;
    }

    public void setIdMittente(long idMittente) {
        this.idMittente = idMittente;
    }

    public long getIdInformazioni() {
        return idInformazioni;
    }

    public void setIdInformazioni(long idInformazioni) {
        this.idInformazioni = idInformazioni;
    }

    public long getIdCuratore() {
        return idCuratore;
    }

    public void setIdCuratore(long idCuratore) {
        this.idCuratore = idCuratore;
    }

}
