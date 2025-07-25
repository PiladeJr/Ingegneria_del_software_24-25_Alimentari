package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.infoAzienda.InfoProduttoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.infoAzienda.InfoTrasformatoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info.InfoAzienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info.InfoAziendaDirector;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.InfoAziendaRepository;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.EnumComuni.Status;
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
public class InfoAziendaService {
    private final InfoAziendaRepository infoAziendaRepository;
    private final UtenteService utenteService;
    private final AziendaService aziendaService;


    public InfoAziendaService(
            InfoAziendaRepository infoAziendaRepository,
            UtenteService utenteService,
            AziendaService aziendaService
    ) {
        this.infoAziendaRepository = infoAziendaRepository;
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

        Optional<Utente> utente = utenteService.getUtenteById(utenteService.getIdUtenteAutenticato());
        if (utente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error: ", "Utente non autenticato"));
        }
        Optional<Azienda> azienda = aziendaService.getAziendaByUtente(utente.get().getId());
        if (azienda.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error: ", "Azienda non trovata per l'utente autenticato"));
        }

        Optional<InfoAzienda> info = infoAziendaRepository.findByAzienda(azienda.get().getId());
        if (info.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error: ", "Informazioni aggiuntive non trovate per l'azienda di ruolo " + utente.get().getRuolo() +" con idAzienda: " + azienda.get().getId()));
        }

        switch (utente.get().getRuolo()){
            case PRODUTTORE -> {
                return ResponseEntity.ok(info.map(InfoProduttoreDTO::new));
            }
            case TRASFORMATORE -> {
                return ResponseEntity.ok(info.map(infoAzienda -> new InfoTrasformatoreDTO(infoAzienda, utente.get().getAziendeCollegate())));
            }
            default -> {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error: ", "Ruolo non autorizzato per ottenere le informazioni aggiuntive"));
            }
        }
    }

    /**
     * <h2>Ottieni le informazioni aggiuntive approvate per un ID specifico</h2>
     * <br/>
     * <p>Metodo responsabile dell'ottenimento delle informazioni aggiuntive approvate per un ID specifico.</p>
     * <p>Se le informazioni sono approvate, vengono restituite in base al ruolo dell'utente associato.</p>
     * <p>In caso di errore, viene restituito un messaggio di errore con lo stato HTTP appropriato.</p>
     *
     * @param id <i>ID</i> delle informazioni aggiuntive da ottenere.
     * @return {@link ResponseEntity} contenente le informazioni aggiuntive approvate o un messaggio di errore.
     */
    public ResponseEntity<?> getInformazioniAggiuntive(long id){
        try {
            InfoAzienda info = getInfoById(id);
            if (info != null) {
                switch (info.getAzienda().getUtente().getRuolo()) {
                    case PRODUTTORE -> {
                        return ResponseEntity.ok(new InfoProduttoreDTO(info));
                    }
                    case TRASFORMATORE -> {
                        return ResponseEntity.ok(new InfoTrasformatoreDTO(info, info.getAzienda().getUtente().getAziendeCollegate()));
                    }
                    default -> {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error: ", "Ruolo non autorizzato per ottenere le informazioni aggiuntive"));
                    }
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error: ", "Informazioni aggiuntive non approvate o non trovate"));
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error: ",e));
        }
    }

    public InfoAzienda getInfoById(long id){
        return infoAziendaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Informazioni aggiuntive non trovate con ID: " + id));
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
        return infoAziendaRepository.save(info);
    }

    /**
     * <h2>Cancella le informazioni aggiuntive di un'azienda</h2>
     * <br/>
     * <p>Metodo responsabile della cancellazione delle informazioni aggiuntive associate all'azienda dell'utente autenticato.</p>
     * <p>Se l'azienda associata all'utente autenticato non viene trovata, viene lanciata un'eccezione.</p>
     * <p>Le informazioni aggiuntive vengono marcate come "ELIMINATO" e dissociate dall'azienda.</p>
     *
     * @throws NoSuchElementException Se l'azienda o le informazioni aggiuntive non vengono trovate.
     */
    public void cancellaInfoAzienda(){
        long idUtente = utenteService.getIdUtenteAutenticato();
        Optional<Azienda> azienda = aziendaService.getAziendaByUtente(idUtente);
        if (azienda.isPresent()) {
            InfoAzienda info = infoAziendaRepository.findByAzienda(azienda.get().getId())
                    .orElseThrow(() -> new NoSuchElementException("Informazioni aggiuntive non trovate per l'azienda con ID: " + azienda.get().getId()));
            info.setAzienda(null);
            info.setStatus(Status.ELIMINATO);
            salvaInformazioniAggiuntive(info);
            aziendaService.saveAzienda(azienda.get());
        } else {
            throw new NoSuchElementException("Nessuna azienda trovata per l'utente con ID: " + idUtente);
        }
    }

    /**
     * <h2>Elimina virtualmente le informazioni aggiuntive</h2>
     * <br/>
     * <p>Metodo, utilizzato dal gestore della piattaforma.</p>
     * <p>Responsabile dell'eliminazione virtuale delle informazioni aggiuntive.</p>
     * <p>Le informazioni vengono dissociate dall'azienda e marcate come "ELIMINATO".</p>
     *
     * @param id <i>ID</i> delle informazioni aggiuntive da eliminare.
     * @throws NoSuchElementException Se le informazioni aggiuntive con l'ID fornito non vengono trovate.
     */
    public void deleteInfoAziendaVirtuale(long id) {
        InfoAzienda info = infoAziendaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Informazioni aggiuntive non trovate con ID: " + id));

        if (info.getStatus() != Status.ELIMINATO) {
            info.setAzienda(null);
            info.setStatus(Status.ELIMINATO);
            salvaInformazioniAggiuntive(info);
        }
        else throw new IllegalStateException("Le informazioni aggiuntive con ID: " + id + " sono già state eliminate.");
    }

    public void deleteInfoAziendaFisico(long id) {
        InfoAzienda info = infoAziendaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Informazioni aggiuntive non trovate con ID: " + id));
        info.setAzienda(null);
        info = salvaInformazioniAggiuntive(info);
        infoAziendaRepository.delete(info);
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
        Optional<Azienda> azienda = aziendaService.getAziendaByUtente(idUtente);

        if (azienda.isPresent()){
            if (azienda.get().getInfoAzienda()!= null){
                throw new IllegalArgumentException("Sono già presenti informazioni aggiuntive per l'azienda associata all'utente autenticato. Effettuare una richiesta di modifica per cambiare le informazioni.");
            }
            InfoAziendaDirector director = new InfoAziendaDirector();
            InfoAzienda info = director.costruisciInfo(
                    descrizione,
                    produzione,
                    metodologie,
                    immagini,
                    certificati,
                    azienda.get());
            info.setVersione(1);
            info.setStatus(Status.PENDING);
            return salvaInformazioniAggiuntive(info);
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
    ) {
        InfoAzienda info = nuoveInformazioniProduttore(descrizione, produzione, metodologie, immagini, certificati);

        long idUtente = utenteService.getIdUtenteAutenticato();

        if (aziendeCollegate != null && !aziendeCollegate.isEmpty()) {
            for (Azienda azienda : aziendeCollegate) {
                if (azienda.getId() < 1) {
                    // Validate denominazioneSociale and indirizzo
                    if (azienda.getDenominazioneSociale() == null || azienda.getDenominazioneSociale().isEmpty()) {
                        throw new IllegalArgumentException("Denominazione sociale obbligatoria per aziende esterne.");
                    }
                    if (azienda.getSedeLegale() == null || azienda.getSedeLegale().getCitta() == null || azienda.getSedeLegale().getCitta().isEmpty()) {
                        throw new IllegalArgumentException("Indirizzo obbligatorio per aziende esterne.");
                    }
                    // Save the new Azienda
                    aziendaService.saveAzienda(azienda);
                } else {
                    // Retrieve existing Azienda
                    Optional<Azienda> existingAzienda = aziendaService.getAziendaById(azienda.getId());
                    if (existingAzienda.isEmpty()) {
                        throw new IllegalArgumentException("Azienda non trovata con ID: " + azienda.getId());
                    }
                }
            }
            aziendaService.collegaAziendeUtente(idUtente, aziendeCollegate);
        } else {
            aziendaService.collegaAziendeUtente(idUtente, Collections.emptyList());
        }

        return info;
    }

}