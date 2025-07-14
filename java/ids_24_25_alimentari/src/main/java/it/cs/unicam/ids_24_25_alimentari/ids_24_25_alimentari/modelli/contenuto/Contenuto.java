package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.EnumComuni.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@MappedSuperclass
public abstract class Contenuto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Setter
    @Column(name = "versione")
    private int versione;

    @Setter
    @Column(name = "id_origine")
    private Long idOrigine;

}
