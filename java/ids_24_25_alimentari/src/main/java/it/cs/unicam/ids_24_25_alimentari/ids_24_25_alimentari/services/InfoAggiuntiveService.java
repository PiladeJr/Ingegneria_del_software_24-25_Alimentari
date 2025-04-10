package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services;


import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders.InformazioniAggiuntiveBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.contenuto.InformazioniAggiuntive;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.UtenteAziendaEsterna;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.InformazioniAggiuntiveRepository;

import org.springframework.stereotype.Service;
import java.io.File;

/**
 * Servizio responsabile della gestione delle informazioni aggiuntive delle aziende
 */
@Service
public class InfoAggiuntiveService {
    private final InformazioniAggiuntiveRepository informazioniAggiuntiveRepository;
    private final UtenteService utenteService;
    private final AziendaService aziendaService;


    public InfoAggiuntiveService(
            InformazioniAggiuntiveRepository informazioniAggiuntiveRepository,
            UtenteService utenteService,
            AziendaService aziendaService
    ) {
        this.informazioniAggiuntiveRepository = informazioniAggiuntiveRepository;
        this.utenteService = utenteService;
        this.aziendaService = aziendaService;
    }

    /**
     * Salva le informazioni aggiuntive nella repository.
     *
     * @param info le informazioni aggiuntive da salvare.
     * @return L'oggetto salvato nella repository.
     */
    public InformazioniAggiuntive salvaInformazioniAggiuntive(InformazioniAggiuntive info) {
        return informazioniAggiuntiveRepository.save(info);
    }

    /**
     * Crea una nuova informazione aggiuntiva per un'azienda, specificando dettagli sulla produzione,
     * le metodologie e allegando immagini e certificati.
     *
     * @param descrizione  Descrizione aggiuntiva dell'azienda.
     * @param produzione   Informazioni sulla produzione dell'azienda.
     * @param metodologie  Metodologie di produzione utilizzate dall'azienda.
     * @param immagini     File contenenti immagini relative alle informazioni aggiuntive.
     * @param certificati  File contenenti eventuali certificazioni dell'azienda.
     * @param idAzienda    Identificativi dell'azienda coinvolta.
     * @return le informazioni aggiuntive salvate nel database.
     * @throws IllegalArgumentException Se si verifica un errore durante il collegamento delle aziende per l'utente autenticato.
     */
    public InformazioniAggiuntive nuovaInformazioneAggiuntiva(
            String descrizione,
            String produzione,
            String metodologie,
            File[] immagini,
            File[] certificati,
            Long[] idAzienda) {

        InformazioniAggiuntiveBuilder builder = new InformazioniAggiuntiveBuilder();
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

        Long idUtente = this.utenteService.getIdUtenteAutenticato();
        Ruolo ruoloUtente = this.utenteService.getRuoloUtenteById(idUtente);

        // Se l'utente è un trasformatore, deve essere collegato alle aziende specificate
        if (ruoloUtente.equals(Ruolo.TRASFORMATORE)) {
            for (long id : idAzienda) {
                UtenteAziendaEsterna collegamento = this.aziendaService.CollegaAzienda(idUtente, id);
                if (collegamento == null) {
                    throw new IllegalArgumentException("Errore durante il collegamento delle aziende");
                }
            }
        }

        return salvaInformazioniAggiuntive(builder.getInformazioniAggiuntive());
    }

}