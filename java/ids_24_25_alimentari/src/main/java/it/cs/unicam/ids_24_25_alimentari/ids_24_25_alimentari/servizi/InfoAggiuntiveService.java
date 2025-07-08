package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;


import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.builders.InformazioniAggiuntiveBuilder;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.InformazioniAggiuntive;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.UtenteAziendaEsterna;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.InformazioniAggiuntiveRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    public ResponseEntity<?> ottieniInformazioniAzienda(){
        try {
            long idUtente = utenteService.getIdUtenteAutenticato();
            Optional<Azienda> azienda = aziendaService.getAziendaByUtente(idUtente);
            Optional<InformazioniAggiuntive> info =informazioniAggiuntiveRepository.findByAzienda(azienda.get().getId());
            if (info.isPresent() && info.get().getApprovato()) {
                return ResponseEntity.ok(info);
            }else return ResponseEntity.ok(" ");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error: ","informazioni non trovate" + e.getMessage()));
        }
    }
    public ResponseEntity<?> getInformazioniAggiuntive(long id){
        try {
            return ResponseEntity.ok(informazioniAggiuntiveRepository.findByIdAndApprovato(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error: ",e));
        }
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
        long idUtente = utenteService.getIdUtenteAutenticato();
        Optional<Azienda> azienda = aziendaService.getAziendaByUtente(idUtente);
        azienda.ifPresent(builder::costruisciAzienda);

        Ruolo ruoloUtente = this.utenteService.getRuoloUtenteById(idUtente);

        // Se l'utente è un trasformatore, deve essere collegato alle aziende specificate
        if (ruoloUtente.equals(Ruolo.TRASFORMATORE)) {
            for (long id : idAzienda) {
                UtenteAziendaEsterna collegamento = this.aziendaService.collegaAzienda(idUtente, id);
                if (collegamento == null) {
                    throw new IllegalArgumentException("Errore durante il collegamento delle aziende");
                }
            }
        }

        return salvaInformazioniAggiuntive(builder.getInformazioniAggiuntive());
    }

}