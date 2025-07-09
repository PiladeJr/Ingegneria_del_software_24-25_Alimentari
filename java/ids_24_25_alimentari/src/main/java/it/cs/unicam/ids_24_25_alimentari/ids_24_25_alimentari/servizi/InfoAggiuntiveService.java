package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;


import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info.InfoAzienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info.InfoAziendaDirector;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.InformazioniAggiuntiveRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.Collections;
import java.util.List;
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

    /**
     * <h2>Ottieni le informazioni aggiuntive approvate di un'azienda</h2>
     * <br/>
     * <p>Metodo responsabile dell'ottenimento delle informazioni aggiuntive di un'azienda.</p>
     * <p>Se le informazioni sono approvate, vengono restituite; altrimenti viene restituito un valore vuoto.</p>
     * <p>In caso di errore, viene restituito un messaggio di errore con lo stato HTTP 404.</p>
     *
     * @return {@link ResponseEntity} contenente le informazioni aggiuntive approvate o un messaggio di errore.
     */
    public ResponseEntity<?> ottieniInformazioniAzienda(){
        try {
            long idUtente = utenteService.getIdUtenteAutenticato();
            Optional<Azienda> azienda = aziendaService.getAziendaByUtente(idUtente);
            Optional<InfoAzienda> info =informazioniAggiuntiveRepository.findByAzienda(azienda.get().getId());
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
     * <h2>Salva le informazioni aggiuntive nella repository</h2>
     * <br/>
     * <p>Metodo responsabile del salvataggio delle informazioni aggiuntive nella repository.</p>
     * <p>Restituisce l'oggetto salvato.</p>
     *
     * @param info <i>Informazioni aggiuntive</i> da salvare.
     * @return <i>Informazioni aggiuntive</i> salvate nella repository.
     */
    public InfoAzienda salvaInformazioniAggiuntive(InfoAzienda info) {
        return informazioniAggiuntiveRepository.save(info);
    }

    /**
     * <h2>Crea nuove informazioni per un produttore</h2>
     * <br/>
     * <p>Metodo responsabile della creazione di nuove informazioni aggiuntive per un produttore.</p>
     * <p>Le informazioni includono descrizione, produzione, metodologie, immagini e certificati.</p>
     * <p>Se l'azienda associata all'utente autenticato non viene trovata, viene lanciata un'eccezione.</p>
     *
     * @param descrizione La descrizione delle informazioni aggiuntive.
     * @param produzione La descrizione della produzione del produttore.
     * @param metodologie Le metodologie utilizzate dal produttore.
     * @param immagini Un array di file contenente le immagini relative al produttore.
     * @param certificati Un array di file contenente i certificati del produttore.
     * @return {@link InfoAzienda} contenente le informazioni aggiuntive salvate.
     * @throws IllegalArgumentException Se l'azienda associata all'utente autenticato non viene trovata.
     */
    public InfoAzienda nuoveInformazioniProduttore(
            String descrizione,
            String produzione,
            String metodologie,
            File[] immagini,
            File[] certificati

    ){
        long idUtente = utenteService.getIdUtenteAutenticato();
        InfoAziendaDirector director = new InfoAziendaDirector();
        Optional<Azienda> azienda = aziendaService.getAziendaByUtente(idUtente);
        if (azienda.isPresent()){
            return salvaInformazioniAggiuntive(
                    director.costruisciInfo(
                    descrizione,
                    produzione,
                    metodologie,
                    immagini,
                    certificati,
                    azienda.get()));
        } else {
            throw new IllegalArgumentException("Azienda non trovata per l'utente autenticato");
        }
    }

    /**
     * <h2>Crea nuove informazioni per un trasformatore</h2>
     * <br/>
     * <p>Metodo responsabile della creazione di nuove informazioni aggiuntive per un trasformatore.</p>
     * <p>Le informazioni includono descrizione, produzione, metodologie, immagini e certificati.</p>
     * <p>Se vengono fornite aziende collegate, queste saranno associate all'utente autenticato.</p>
     * <p>Se non vengono fornite aziende collegate, verrà associata una lista vuota.</p>
     *
     * @param descrizione La descrizione delle informazioni aggiuntive.
     * @param produzione La descrizione della produzione del trasformatore.
     * @param metodologie Le metodologie utilizzate dal trasformatore.
     * @param immagini Un array di file contenente le immagini relative al trasformatore.
     * @param certificati Un array di file contenente i certificati del trasformatore.
     * @param aziendeCollegate Una lista di aziende collegate al processo di trasformazione.
     * @return {@link InfoAzienda} contenente le informazioni aggiuntive salvate.
     */
    public InfoAzienda nuoveInformazioniTrasformatore(
            String descrizione,
            String produzione,
            String metodologie,
            File[] immagini,
            File[] certificati,
            List<Azienda> aziendeCollegate
    ){
        InfoAzienda info = nuoveInformazioniProduttore( descrizione, produzione, metodologie, immagini, certificati);
        long idUtente = utenteService.getIdUtenteAutenticato();
        if (aziendeCollegate != null && !aziendeCollegate.isEmpty()) {
            aziendaService.collegaAziendeUtente(idUtente, aziendeCollegate);
        }
        else {
            aziendaService.collegaAziendeUtente(idUtente, Collections.emptyList());
        }
        return info;
    }

}