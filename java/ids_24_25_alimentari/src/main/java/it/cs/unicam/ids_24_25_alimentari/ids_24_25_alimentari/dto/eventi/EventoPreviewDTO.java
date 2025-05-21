package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.eventi;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.LocalDateTime;

@Getter
@Setter
public class EventoPreviewDTO {
    private Long id;
    private String titolo;
    private File locandina;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;

    public EventoPreviewDTO(Long id, String titolo, File locandina, LocalDateTime dataInizio, LocalDateTime dataFine) {
        this.id = id;
        this.titolo = titolo;
        this.locandina = locandina;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }
}
