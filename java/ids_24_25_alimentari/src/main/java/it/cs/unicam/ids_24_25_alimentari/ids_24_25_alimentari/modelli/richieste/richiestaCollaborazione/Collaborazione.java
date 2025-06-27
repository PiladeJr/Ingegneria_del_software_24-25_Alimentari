package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.richieste.richiestaCollaborazione;

import java.io.File;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.Contenuto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "richiestacollaborazione")
@NoArgsConstructor
public class Collaborazione extends Contenuto {

    private String nome;

    private String cognome;

    private String telefono;

    private String email;

    private Ruolo ruolo;

    @Column(name = "iban", nullable = false)
    private String iban;

    @Column(name = "cartaIdentita")
    private File cartaIdentita;

    //---------------------campi azienda---------------------

    private String denominazioneSociale;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sede_legale_id")
    private Indirizzo sedeLegale;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sede_operativa_id")
    private Indirizzo sedeOperativa;

    @Column(name = "iva", nullable = true)
    private String iva;

    @Column(name = "certificato")
    private File certificato;

    //---------------------campi curatore---------------------

    @Column(name = "curriculum")
    private File cv;

}
