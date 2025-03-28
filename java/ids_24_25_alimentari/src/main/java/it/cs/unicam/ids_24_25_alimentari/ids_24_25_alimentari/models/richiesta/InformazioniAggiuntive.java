package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.Contenuto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class InformazioniAggiuntive extends Contenuto {

    private String descrizioneAzienda;
    private String descrizioneProduzione;
    private String descrizioneMetodi;

    @ElementCollection
    @CollectionTable(name = "immagini_info-aggiuntive", joinColumns = @JoinColumn(name = "contenuto_id"))
    @Column(name = "immagine")
    private List<File> immagini;
    @ElementCollection
    @CollectionTable(name = "certificati", joinColumns = @JoinColumn(name = "contenuto_id"))
    @Column(name = "certificato")
    private List<File> certificati;

}
