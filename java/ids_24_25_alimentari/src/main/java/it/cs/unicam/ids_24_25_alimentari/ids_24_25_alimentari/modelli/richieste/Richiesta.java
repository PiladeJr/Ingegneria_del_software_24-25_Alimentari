package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.EnumComuni.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Richiesta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "approvato")
    private Boolean approvato;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

}
