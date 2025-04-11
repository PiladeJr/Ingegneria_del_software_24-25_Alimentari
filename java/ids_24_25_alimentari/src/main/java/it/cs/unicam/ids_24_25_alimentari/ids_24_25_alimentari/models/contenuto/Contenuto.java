package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.contenuto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@MappedSuperclass
public abstract class Contenuto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Column(name = "approvato")
    private Boolean approvato;

}
