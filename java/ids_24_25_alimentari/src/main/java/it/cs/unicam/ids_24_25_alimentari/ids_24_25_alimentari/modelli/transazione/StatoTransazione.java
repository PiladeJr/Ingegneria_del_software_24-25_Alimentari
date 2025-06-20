package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.transazione;

public enum StatoTransazione {

    IN_ATTESA("In attesa"),
    COMPLETATA("Completata"),
    ANNULLATA("Annullata"),
    FALLITA("Fallita");

    private final String descrizione;

    StatoTransazione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }
}
