package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders.InformazioniAggiuntiveBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders.RichiestaBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.InformazioniAggiuntive;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.InformazioniAggiuntiveRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.RichiestaRepository;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ContenutoService {
    private final RichiestaRepository richiestaRepository;
    private final InformazioniAggiuntiveRepository informazioniAggiuntiveRepository;
    InformazioniAggiuntiveBuilder builder;
    RichiestaBuilder richiestaBuilder;

    public ContenutoService(RichiestaRepository richiestaRepository, InformazioniAggiuntiveRepository informazioniAggiuntiveRepository) {
        this.richiestaRepository = richiestaRepository;
        this.informazioniAggiuntiveRepository = informazioniAggiuntiveRepository;
    }



    public Richiesta salvaRichiesta(Richiesta richesta){
        return richiestaRepository.save(richesta);
    }
    public Richiesta nuovaRichiestaInformazioni(Tipologia tipo, Long idMittente, String descrizione, String produzione, String metodologie, List<File>immagini, List<File>certificati){
        RichiestaBuilder richiesta=new RichiestaBuilder();
        richiesta.costruisciTipologia(tipo);
        richiesta.costruisciIdMittente(idMittente);
        if(tipo.equals("InfoAzienda")){
            //TODO cambiare a modo di switch per i casi Prodotto e Evento
            long id =nuovaInformazioneAggiuntiva(descrizione, produzione, metodologie, immagini, certificati).getId();
            richiesta.costruisciIdInformazioni(id);
        }
        return salvaRichiesta(richiesta.build());
    }
    public InformazioniAggiuntive salvaInformazioniAggiuntive(InformazioniAggiuntive info){
        return informazioniAggiuntiveRepository.save(info);
    }
    public InformazioniAggiuntive nuovaInformazioneAggiuntiva(String descrizione,
                                                              String produzione,
                                                              String metodologie,
                                                              List<File> immagini,
                                                              List<File> certificati){
        InformazioniAggiuntiveBuilder builder=new InformazioniAggiuntiveBuilder();
        builder.costruisciDescrizione(descrizione);
        builder.costruisciProduzione(produzione);
        builder.costruisciMetodi(metodologie);
        if (immagini != null) {
            for (File immagine : immagini) {
                builder.aggiungiImmagine(immagine);
            }
        }
        if (certificati != null) {
            for (File certificato : certificati) {
                builder.aggiungiCertificato(certificato);
            }
        }
        return salvaInformazioniAggiuntive(builder.getInformazioniAggiuntive());
    }
}
