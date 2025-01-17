package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Entity;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.Enum.Tipologia;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Richiesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Tipologia tipologia;

    @Column(nullable = false)
    private boolean approvato;

    @Column(nullable = false)
    private long idMittente;

    public long getId() { return id; }

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

    public boolean isApprovato() {
        return approvato;
    }

    public long getIdMittente() {
        return idMittente;
    }

    public void setIdMittente(int idMittente) {
        this.idMittente = idMittente;
    }

}
