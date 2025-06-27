package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.transazione;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "transazione")
public class Transazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "importo", nullable = false)
    private double importo;
    @Column(name = "data_transazione", nullable = false)
    private LocalDateTime dataTransazione;
    @Column(name = "stato_transazione", nullable = false)
    private StatoTransazione statoTransazione;
    @Column(name = "metodo_pagamento", nullable = false)
    private MetodiPagamentoTransazione metodoPagamento;

}
