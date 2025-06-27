package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.Richieste.Contenuto.StrategyContenuto;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaContenuto.Tipologia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class RichiestaStrategyFactory {
    private final Map<Tipologia, RichiestaContenutoStrategy> strategiaPerTipologia;

    @Autowired
    public RichiestaStrategyFactory(List<RichiestaContenutoStrategy> strategies) {
        this.strategiaPerTipologia = new EnumMap<>(Tipologia.class);
        for (RichiestaContenutoStrategy strategy : strategies) {
            strategiaPerTipologia.put(strategy.getTipologia(), strategy);
        }
    }

    public RichiestaContenutoStrategy getStrategy(Tipologia tipologia) {
        RichiestaContenutoStrategy strategy = strategiaPerTipologia.get(tipologia);
        if (strategy == null) {
            throw new IllegalArgumentException("Nessuna strategia trovata per la tipologia: " + tipologia);
        }
        return strategy;
    }}
