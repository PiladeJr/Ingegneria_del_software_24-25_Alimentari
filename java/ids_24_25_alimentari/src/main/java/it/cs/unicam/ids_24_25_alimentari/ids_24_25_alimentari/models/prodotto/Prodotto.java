package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.prodotto;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.File;
import java.util.List;

@Entity
@Table(name = "prodotti")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Prodotto implements Acquistabile{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "id_azienda")
    private long idAzienda;

    @ElementCollection
    @CollectionTable(name = "immagini", joinColumns = @JoinColumn(name = "richiesta_id"))
    @Column(name = "immagine")
    private List<File> immagini;

    @Column(name = "prezzo", nullable = false)
    private double prezzo;

    @Column(name = "quantità", nullable = false)
    private int quantita;

    @Column(name = "allergeni")
    private String allergeni;

    @Column(name = "tecniche")
    private String tecniche;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public long getIdAzienda() {
        return idAzienda;
    }

    public void setIdAzienda(long idAzienda) {
        this.idAzienda = idAzienda;
    }

    public List<File> getImmagini() {
        return immagini;
    }

    public void setImmagini(List<File> immagini) {
        this.immagini = immagini;
    }

    @Override
    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getAllergeni() {
        return allergeni;
    }

    public void setAllergeni(String allergeni) {
        this.allergeni = allergeni;
    }

    public String getTecniche() {
        return tecniche;
    }

    public void setTecniche(String tecniche) {
        this.tecniche = tecniche;
    }

}
