package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("FIERA")
@NoArgsConstructor
public class EventoFiera extends Evento{
    @ManyToMany
    @JoinTable(
            name = "evento_fiera_aziende",
            joinColumns = @JoinColumn(name = "evento_fiera_id"),
            inverseJoinColumns = @JoinColumn(name = "azienda_id")
    )
    private List<Azienda> aziendePresenti = new ArrayList<>();
}
