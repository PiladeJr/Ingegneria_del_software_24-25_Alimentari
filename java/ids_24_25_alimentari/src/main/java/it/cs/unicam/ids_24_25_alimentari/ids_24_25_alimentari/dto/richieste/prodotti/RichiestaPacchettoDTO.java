package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.prodotti;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class RichiestaPacchettoDTO {
    @NotNull(message = "Nome prodotto obbligatorio")
    private String nome;
    private String descrizione;
    @NotNull(message = "Prezzo prodotto obbligatorio")
    private Double prezzo;
    @NotNull(message = "Pacchetto non può essere vuoto")
    private Set<Long> prodotti;

}
