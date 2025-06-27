package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RichiestaCollaborazione extends Richiesta {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ruolo ruolo;
    @OneToOne
    @JoinColumn(name = "collaborazione_id", referencedColumnName = "id")
    private Collaborazione collaborazione;
}
