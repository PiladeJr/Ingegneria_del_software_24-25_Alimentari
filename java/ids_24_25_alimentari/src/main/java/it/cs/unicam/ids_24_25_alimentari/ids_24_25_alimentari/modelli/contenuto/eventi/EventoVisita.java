package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;

@Entity
@Getter
@Setter
@DiscriminatorValue("VISITA")
@NoArgsConstructor
public class EventoVisita extends Evento{
    @ManyToOne
    @JoinColumn(name = "azienda_riferimento_id", nullable = true)
    private Azienda aziendaRiferimento;
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(
            name = "evento_visita_iscritti",
            joinColumns = @JoinColumn(name = "evento_visita_id"),
            inverseJoinColumns = @JoinColumn(name = "utente_id")
    )
    private List<Utente> iscritti;
}
