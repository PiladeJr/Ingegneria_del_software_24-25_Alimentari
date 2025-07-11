package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.Contenuto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InfoAzienda extends Contenuto {

    @Column(name = "descrizione", length = 400)
    private String descrizioneAzienda;

    @Column(name = "produzione", length = 400)
    private String descrizioneProduzione;

    @Column(name = "metodi", length = 400)
    private String descrizioneMetodi;

    @ElementCollection
    @CollectionTable(name = "immagini_info_aggiuntive", joinColumns = @JoinColumn(name = "contenuto_id"))
    @Column(name = "immagine")
    private List<File> immagini;

    @ElementCollection
    @CollectionTable(name = "certificati", joinColumns = @JoinColumn(name = "contenuto_id"))
    @Column(name = "certificato")
    private List<File> certificati;

    @OneToOne
    @JoinColumn(name = "azienda_id")
    private Azienda azienda;

}
