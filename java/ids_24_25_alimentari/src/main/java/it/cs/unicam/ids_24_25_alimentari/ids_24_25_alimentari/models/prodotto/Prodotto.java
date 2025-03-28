package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.prodotto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.Contenuto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.File;
import java.util.List;

@Entity
@Table(name = "prodotto")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Prodotto extends Contenuto implements Acquistabile{

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "id_azienda", nullable = false)
    private Long idAzienda;

    @ElementCollection
    @CollectionTable(name = "immagini_prodotto", joinColumns = @JoinColumn(name = "contenuto_id", referencedColumnName = "id"))
    @Column(name = "immagine")
    private List<File> immagini;

    @Column(name = "prezzo", nullable = false)
    private Double prezzo;

    @Column(name = "quantità", nullable = false)
    private Integer quantita;

    @Column(name = "allergeni")
    private String allergeni;

    @Column(name = "tecniche")
    private String tecniche;


    @Override
    public Double getPrezzo() {
        return this.prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }


}
