package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "richiesta")
@Getter
@Setter
@NoArgsConstructor
public class Richiesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Tipologia tipologia;

    @Column(nullable = true)
    private Boolean approvato;

    @Column(nullable = false)
    private long idMittente;
    private long idInformazioni;
    private long idCuratore;

}
