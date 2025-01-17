package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Richiesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Tipologia tipologia;

    @Column(nullable = false)
    private boolean approvato;

    @Column(nullable = false)
    private int idMittente;

    public Richiesta(Tipologia tipologia, boolean approvato, int idMittente) {
        this.tipologia = tipologia;
        this.approvato = approvato;
        this.idMittente = idMittente;
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

    public boolean isApprovato() {
        return approvato;
    }

    public int getIdMittente() {
        return idMittente;
    }

    public void setIdMittente(int idMittente) {
        this.idMittente = idMittente;
    }

}
