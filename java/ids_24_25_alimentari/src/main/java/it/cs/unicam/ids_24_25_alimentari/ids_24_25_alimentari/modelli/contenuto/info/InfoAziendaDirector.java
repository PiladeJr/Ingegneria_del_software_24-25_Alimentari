package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.InfoAziendaBuilder;

import java.io.File;

public class InfoAziendaDirector {

    private final InfoAziendaBuilder builder = new InfoAziendaBuilder();

    public InfoAzienda costruisciInfo(
            String descrizioneAzienda,
            String descrizioneProduzione,
            String descrizioneMetodi,
            File[] immagini,
            File[] certificati,
            Azienda azienda) {

        builder.costruisciDescrizione(descrizioneAzienda);
        builder.costruisciProduzione(descrizioneProduzione);
        builder.costruisciMetodi(descrizioneMetodi);

        for (File immagine : immagini) {
            builder.aggiungiImmagine(immagine);
        }
        for (File certificato : certificati) {
            builder.aggiungiCertificato(certificato);
        }
        builder.costruisciAzienda(azienda);
        return builder.build();
    }

}
