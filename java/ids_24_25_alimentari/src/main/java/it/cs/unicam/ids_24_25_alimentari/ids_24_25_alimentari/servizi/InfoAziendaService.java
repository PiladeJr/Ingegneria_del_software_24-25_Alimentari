package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;


import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.infoAzienda.InfoProduttoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.infoAzienda.InfoTrasformatoreDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info.InfoAzienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.info.InfoAziendaDirector;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
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

//            if (utente.get().getRuolo().equals(Ruolo.PRODUTTORE)) {
//                if (info.isEmpty()) {
//                    return ResponseEntity.status(HttpStatus.OK).body(" Nessuna informazione presente per il produttore");
//                }
//                return ResponseEntity.ok(info.map(InfoProduttoreDTO::new));
//            }
//
//            if (utente.get().getRuolo().equals(Ruolo.TRASFORMATORE)) {
//                if (info.isEmpty()) {
//                    return ResponseEntity.status(HttpStatus.OK).body(" Nessuna informazione presente per il trasformatore");
//                }
//                return ResponseEntity.ok(info.map(infoAzienda -> new InfoTrasformatoreDTO(infoAzienda, utente.get().getAziendeCollegate())));
//            }
//            else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error: ", "Ruolo non autorizzato per ottenere le informazioni aggiuntive"));

    }

    public ResponseEntity<?> getInformazioniAggiuntive(long id){
        try {
            return ResponseEntity.ok(infoAziendaRepository.findByIdAndApprovato(id));
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
        return infoAziendaRepository.save(info);
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