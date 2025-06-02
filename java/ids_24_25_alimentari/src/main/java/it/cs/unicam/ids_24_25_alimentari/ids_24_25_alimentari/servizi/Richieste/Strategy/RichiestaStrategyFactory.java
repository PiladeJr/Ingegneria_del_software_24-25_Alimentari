package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Strategy;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richiesta.Tipologia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class RichiestaStrategyFactory {
    private final Map<Tipologia, RichiestaStrategy> strategiaPerTipologia;

    @Autowired
    public RichiestaStrategyFactory(List<RichiestaStrategy> strategies) {
        this.strategiaPerTipologia = new EnumMap<>(Tipologia.class);
        for (RichiestaStrategy strategy : strategies) {
            if (strategy instanceof StrategyInformazioniAggiuntive) {
                strategiaPerTipologia.put(Tipologia.INFO_AZIENDA, strategy);
            } else if (strategy instanceof StrategyProdotto) {
                strategiaPerTipologia.put(Tipologia.PRODOTTO, strategy);
            } else if (strategy instanceof StrategyEvento) {
                strategiaPerTipologia.put(Tipologia.EVENTO, strategy);
            }
        }
    }

    public RichiestaStrategy getStrategy(Tipologia tipologia) {
        RichiestaStrategy strategy = strategiaPerTipologia.get(tipologia);
        if (strategy == null) {
            throw new IllegalArgumentException("Nessuna strategia trovata per la tipologia: " + tipologia);
        }
        return strategy;
    }}
