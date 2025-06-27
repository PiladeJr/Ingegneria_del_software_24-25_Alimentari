package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public abstract class Richiesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "approvato")
    private Boolean approvato;


}
