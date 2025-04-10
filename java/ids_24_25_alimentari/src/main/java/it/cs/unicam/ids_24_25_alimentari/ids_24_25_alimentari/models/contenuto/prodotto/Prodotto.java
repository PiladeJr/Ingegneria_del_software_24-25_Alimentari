package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.contenuto.prodotto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.contenuto.Contenuto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "prodotto")
@AllArgsConstructor
@NoArgsConstructor
public abstract class Prodotto extends Contenuto {

    @Column(name = "nome")
    protected String nome;

    @Column(name = "descrizione")
    protected String descrizione;

    @Column(name = "prezzo")
    protected Double prezzo;

    abstract Double getPrezzo();
}
