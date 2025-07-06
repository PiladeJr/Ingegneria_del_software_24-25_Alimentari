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
    @Enumerated(EnumType.STRING)
    @Column(name = "stato_transazione", nullable = false)
    private StatoTransazione statoTransazione;
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento", nullable = false)
    private MetodiPagamentoTransazione metodoPagamento;

    @Column(name = "paypal_payment_id", nullable = true)
    private String paypalPaymentId;

}
