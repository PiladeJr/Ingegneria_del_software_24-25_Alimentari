package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ManyToAny;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "evento_fiera")
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
