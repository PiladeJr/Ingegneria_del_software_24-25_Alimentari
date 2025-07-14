package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste.eventi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RichiestaEventoFieraDTO extends RichiestaEventoDTO {
    private List<Azienda> aziendePresenti = new ArrayList<>();

}
