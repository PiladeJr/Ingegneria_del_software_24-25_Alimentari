package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.prodotto.ProdottoSingolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info.InfoAzienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.File;
import java.util.List;

@Entity
@Table(name = "aziende")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Azienda {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "denominazione_sociale", nullable = false)
    private String denominazioneSociale;

    @ManyToOne
    @JoinColumn(name = "indirizzoSedeLegale_id", nullable = false)
    private Indirizzo sedeLegale;

    @ManyToOne
    @JoinColumn(name = "indirizzoSedeOperativa_id", nullable = false)
    private Indirizzo sedeOperativa;

    @Column(name = "iva", nullable = false)
    private String iva;

    @Column(name = "certificato")
    private File certificato;

    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "prodotto", nullable = true)
    private List<ProdottoSingolo> prodotti;

    @OneToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @OneToOne(mappedBy = "azienda", cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    private InfoAzienda infoAzienda;

    @ManyToMany(mappedBy = "aziendeCollegate")
    private List<Utente> utentiCollegati;

}
