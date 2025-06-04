package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "richiesta")
@NoArgsConstructor
@Setter
@Getter
public class Richiesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tipologia tipologia;

    private String tipoContenuto;

    @Column(nullable = true)
    private Boolean approvato;

    @Column(nullable = false)
    private long idMittente;

    @Column(nullable = false)
    private long targetId;

    private long idCuratore;

}
