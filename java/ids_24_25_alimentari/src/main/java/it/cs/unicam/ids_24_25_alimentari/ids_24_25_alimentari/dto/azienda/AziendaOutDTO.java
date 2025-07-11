package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.azienda;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.infoAzienda.InfoProduttoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.infoAzienda.InfoTrasformatoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info.InfoAzienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class AziendaOutDTO {
    private long id;
    private String denominazioneSociale;
    private Indirizzo sedeLegale;
    private Indirizzo sedeOperativa;
    private String iva;
    private File certificato;
    private InfoProduttoreDTO infoAzienda;

    public AziendaOutDTO(Azienda azienda){
        this.id = azienda.getId();
        this.denominazioneSociale = azienda.getDenominazioneSociale();
        this.sedeLegale = azienda.getSedeLegale();
        this.sedeOperativa = azienda.getSedeOperativa();
        this.iva = azienda.getIva();
        this.certificato = azienda.getCertificato();
        if (azienda.getInfoAzienda()==null){
            this.infoAzienda = null;
        }
        else {
            if (azienda.getUtente().getRuolo()== Ruolo.TRASFORMATORE) {
                this.infoAzienda = new InfoTrasformatoreDTO(azienda.getInfoAzienda(), azienda.getUtente().getAziendeCollegate());
            }
            else this.infoAzienda = new InfoProduttoreDTO(azienda.getInfoAzienda());
        }
    }
}
