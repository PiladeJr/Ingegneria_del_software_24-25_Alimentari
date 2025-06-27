package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.Richiesta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("CONTENUTO")
@NoArgsConstructor
@Setter
@Getter
public class RichiestaContenuto extends Richiesta {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tipologia tipologia;

    private String tipoContenuto;

    @Column(nullable = false)
    private long idMittente;

    @Column(nullable = false)
    private long targetId;

    private long idCuratore;

}
