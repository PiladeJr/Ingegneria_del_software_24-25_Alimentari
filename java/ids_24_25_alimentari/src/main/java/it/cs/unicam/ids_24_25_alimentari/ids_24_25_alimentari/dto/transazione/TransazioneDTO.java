package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.transazione;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.transazione.MetodiPagamentoTransazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.transazione.StatoTransazione;
import lombok.Data;

@Data
public class TransazioneDTO {
    private double importo;
    private MetodiPagamentoTransazione metodoPagamento;
    private StatoTransazione statoTransazione;
}
