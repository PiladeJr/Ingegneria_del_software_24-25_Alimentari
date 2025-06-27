package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.ordine;

public enum StatoOrdine {

    IN_ATTESA("In attesa di pagamento"),
    IN_PREPARAZIONE("In preparazione"),
    PRONTO("Pronto"),
    CONSEGNATO("Consegnato"),
    ANNULLATO("Annullato");

    private final String descrizione;

    StatoOrdine(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }

}
