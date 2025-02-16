package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.RichiestaCollaborazioneAziendaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.Richieste.RichiestaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.Richieste.RichiestaInformazioniDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.InformazioniAggiuntive;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.Richiesta;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.richiesta.Tipologia;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.AziendaService;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.ContenutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.stream.Collectors;

import static it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.ConvertitoreMultipartFileToFile.convertiMultipartFileToFile;
import static it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.ConvertitoreMultipartFileArrayToFileArray.convertMultipartFileArrayToFileArray;

/**
 * Controller for handling operations related to Azienda.
 * It provides endpoints for fetching, saving, deleting, and creating Azienda
 * entities.
 */
@RestController
@RequestMapping("/api/azienda")
public class AziendaController {

    @Autowired
    AziendaService aziendaService = new AziendaService();
    @Autowired
    ContenutoService contentutoService;

    public AziendaController(ContenutoService contentutoService) {
        this.contentutoService = contentutoService;
    }

    /**
     * restituisce una lista di tutte le aziende salvate dalla piattaforma
     *
     * @return ResponseEntity contenente la lista di tutte le aziende.
     */
    @GetMapping
    public ResponseEntity<List<Azienda>> getAllAziende() {
        List<Azienda> aziende = aziendaService.getAllAziende();
        return ResponseEntity.ok(aziende);
    }

    /**
     * ottiene un'azienda dal suo id.
     *
     * @param id l'ID della Azienda.
     * @return ResponseEntity contenente l'azienda se trovata, oppure un 404 status
     *         se
     *         non trovata, oppure un errore 500 in caso di eccezione.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAziendaById(@PathVariable Long id) {
        try {
            Azienda azienda = aziendaService.getAziendaById(id).orElse(null);
            if (azienda == null) {
                return ResponseEntity.status(404)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\": \"Azienda non trovata\"}");
            }
            return ResponseEntity.ok(azienda);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore interno del server: " + e.getMessage());
        }
    }

    /**
     * ottiene la lista di aziende selezionate in base al filtro
     * ruolo dell'utente
     *
     * @param ruolo il ruolo dell'utente in base a cui filtrare le aziende
     * @return la lista di aziende appartenenti ad utenti con il ruolo specificato
     */
    @GetMapping("/roles")
    public ResponseEntity<List<Azienda>> getAziendeByRuolo(@PathVariable Ruolo ruolo) {
        List<Azienda> aziende = aziendaService.getAziendeByRuolo(ruolo);
        return ResponseEntity.ok(aziende);
    }

    /**
     * salva una nuova entità Azienda.
     *
     * @param azienda l'oggetto Azienda da salvare.
     * @return ResponseEntity contenente l'azienda salvata.
     */
    @PostMapping
    public ResponseEntity<Azienda> saveAzienda(@RequestBody Azienda azienda) {
        Azienda saveAzienda = aziendaService.saveAzienda(azienda);
        return ResponseEntity.ok(saveAzienda);
    }

    /**
     * Deletes an Azienda entity by its ID.
     *
     * @param id The ID of the Azienda to be deleted.
     * @return ResponseEntity with no content if deletion is successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAzienda(@PathVariable Long id) {
        aziendaService.deleteAzienda(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Creates a new Azienda entity with a multipart file upload.
     * This endpoint expects a RichiestaCollaborazioneAziendaDTO containing detailed
     * information and a file.
     *
     * @param collaborazione The DTO containing Azienda details and file data.
     * @return ResponseEntity containing the created Azienda.
     */
    @PostMapping(value = "/azienda", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Azienda> createAzienda(@RequestBody RichiestaCollaborazioneAziendaDTO collaborazione) {
        try {
            Azienda azienda = aziendaService.createAzienda(
                    collaborazione.getDenSociale(),
                    collaborazione.getSedeLegale(),
                    collaborazione.getSedeOperativa(),
                    collaborazione.getIva(),
                    collaborazione.getIban(),
                    convertiMultipartFileToFile(collaborazione.getCertificato()));
            return ResponseEntity.ok(azienda);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/informazioni/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Richiesta> createAziendaInformazioni(
            @RequestParam("idMittente") Long idMittente,
            @RequestParam("descrizione") String descrizione,
            @RequestParam("produzione") String produzione,
            @RequestParam("metodologie") String metodologie,
            @RequestParam("immagini") MultipartFile[] immagini,
            @RequestParam("certificati") MultipartFile[] certificati) {

        File[] immaginiFiles;
        try {
            immaginiFiles = convertMultipartFileArrayToFileArray(immagini);
        } catch (IOException e) {
            System.err.println("Errore durante la conversione delle immagini: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error converting multipart file array", e);
        }

        File[] certificatiFiles;
        try {
            certificatiFiles = convertMultipartFileArrayToFileArray(certificati);
        } catch (IOException e) {
            System.err.println("Errore durante la conversione dei certificati: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error converting multipart file array", e);
        }

        try {
            Richiesta richiesta = contentutoService.nuovaRichiestaInformazioni(
                    Tipologia.valueOf("InfoAzienda"),
                    idMittente,
                    descrizione,
                    produzione,
                    metodologie,
                    immaginiFiles,
                    certificatiFiles);

            return ResponseEntity.ok(richiesta);
        } catch (Exception e) {
            System.err.println("Errore durante la creazione della richiesta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Errore durante la creazione della richiesta", e);
        }
    }
}