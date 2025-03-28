package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@MappedSuperclass
public abstract class Contenuto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "approvato")
    private Boolean approvato;

}
