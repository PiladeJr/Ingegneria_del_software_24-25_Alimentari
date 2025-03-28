package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.prodotto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.File;
import java.util.List;

@Entity
@Table(name = "prodotto")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Prodotto implements Acquistabile{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "id_azienda", nullable = false)
    private Long idAzienda;

    @ElementCollection
    @CollectionTable(name = "immagini", joinColumns = @JoinColumn(name = "contenuto_id"))
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

    public List<File> getImmagini() {
        return immagini;
    }

    public void setImmagini(List<File> immagini) {
        this.immagini = immagini;
    }

    @Override
    public Double getPrezzo() {
        return this.prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }


}
