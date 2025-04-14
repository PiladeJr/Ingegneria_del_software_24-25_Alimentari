package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richieste;

public class CambiaStatoRichiestaCollaborazioneDTO {

    private Boolean stato;
    private String messaggioAggiuntivo;
    private Long id;

    public Long getId() {
        return id;
    }

    public CambiaStatoRichiestaCollaborazioneDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getMessaggioAggiuntivo() {
        return messaggioAggiuntivo;
    }

    public CambiaStatoRichiestaCollaborazioneDTO setMessaggioAggiuntivo(String messaggioAggiuntivo) {
        this.messaggioAggiuntivo = messaggioAggiuntivo;
        return this;
    }

    public Boolean getStato() {
        return stato;
    }

    public CambiaStatoRichiestaCollaborazioneDTO setStato(Boolean stato) {
        this.stato = stato;
        return this;
    }

}
