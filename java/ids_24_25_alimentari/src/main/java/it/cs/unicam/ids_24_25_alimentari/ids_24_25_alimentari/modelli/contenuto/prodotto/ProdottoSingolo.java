package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.File;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "prodotto_singolo")
public class ProdottoSingolo extends Prodotto {

    @Column(name = "id_azienda", nullable = false)
    private Long idAzienda;

    @ElementCollection
    @CollectionTable(name = "immagini_prodotto", joinColumns = @JoinColumn(name = "contenuto_id", referencedColumnName = "id"))
    @Column(name = "immagine")
    private List<File> immagini;

    @Column(name = "quantità", nullable = false)
    private Integer quantita;

    @Column(name = "allergeni")
    private String allergeni;

    @Column(name = "tecniche")
    private String tecniche;

    public ProdottoSingolo(String nome, String descrizione, Double prezzo, Long idAzienda, List<File> immagini, Integer quantita, String allergeni, String tecniche) {
        super(nome, descrizione, prezzo);
        this.idAzienda = idAzienda;
        this.immagini = immagini;
        this.quantita = quantita;
        this.allergeni = allergeni;
        this.tecniche = tecniche;
    }

    @Override
    Double getPrezzo() {
        return this.prezzo;
    }
}
