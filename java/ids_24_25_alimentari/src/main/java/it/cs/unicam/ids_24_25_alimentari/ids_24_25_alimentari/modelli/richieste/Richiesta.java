package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "tipo_richiesta", discriminatorType = DiscriminatorType.STRING)
@SequenceGenerator(name = "richiesta_seq", sequenceName = "richiesta_sequence", allocationSize = 1)
public abstract class Richiesta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "richiesta_seq")
    private long id;
    @Column(name = "approvato")
    private Boolean approvato;

}
