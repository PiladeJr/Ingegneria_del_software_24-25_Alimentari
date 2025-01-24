package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.RichiestaCollaborazioneAziendaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.RichiestaCollaborazione;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.RichiesteCollaborazioneService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.Ruolo;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/richieste-collaborazione")
public class RichiestaCollaborazioneController {

    @Autowired
    private RichiesteCollaborazioneService richiesteCollaborazioneService;

    @GetMapping
    public ResponseEntity<List<RichiestaCollaborazione>> getAllRichieste() {
        List<RichiestaCollaborazione> richieste = richiesteCollaborazioneService.getAllRichieste();
        return ResponseEntity.ok(richieste);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RichiestaCollaborazione> getRichiestaById(@PathVariable Long id) {
        return richiesteCollaborazioneService.getRichiestaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RichiestaCollaborazione> createRichiesta(@RequestBody RichiestaCollaborazione richiesta) {
        RichiestaCollaborazione savedRichiesta = richiesteCollaborazioneService.saveRichiesta(richiesta);
        return ResponseEntity.ok(savedRichiesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRichiesta(@PathVariable Long id) {
        richiesteCollaborazioneService.deleteRichiesta(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/azienda", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RichiestaCollaborazione> creaRichiestaAzienda(
            @ModelAttribute RichiestaCollaborazioneAziendaDTO richiestaAziendaDTO) {

        try {
            // Converti il MultipartFile in File
            File fileCertificato = convertiMultipartFileToFile(richiestaAziendaDTO.getCertificato());

            // Chiama il servizio con i parametri e il file convertito
            RichiestaCollaborazione richiesta = richiesteCollaborazioneService.creaRichiestaAzienda(
                    richiestaAziendaDTO.getNome(),
                    richiestaAziendaDTO.getCognome(),
                    richiestaAziendaDTO.getTelefono(),
                    richiestaAziendaDTO.getEmail(),
                    richiestaAziendaDTO.getRuolo(),
                    richiestaAziendaDTO.getDenSociale(),
                    richiestaAziendaDTO.getSedeLegale(),
                    richiestaAziendaDTO.getSedeOperativa(),
                    richiestaAziendaDTO.getIban(),
                    richiestaAziendaDTO.getIva(),
                    fileCertificato);

            return ResponseEntity.ok(richiesta);
        } catch (Exception e) {
            // Gestione errori
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping(value = "/animatore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RichiestaCollaborazione> creaRichiestaAnimatore(
            @RequestParam String nome,
            @RequestParam String cognome,
            @RequestParam String telefono,
            @RequestParam String email,
            @RequestParam Ruolo ruolo,
            @RequestParam String aziendaReferente,
            @RequestParam String iban,
            @RequestPart("cartaIdentita") MultipartFile cartaIdentita) {

        try {
            // Converti il MultipartFile in un File
            File fileCartaIdentita = convertiMultipartFileToFile(cartaIdentita);

            // Chiama il servizio con i parametri e il file convertito
            RichiestaCollaborazione richiesta = richiesteCollaborazioneService.creaRichiestaAnimatore(
                    nome, cognome, telefono, email, ruolo, aziendaReferente, iban, fileCartaIdentita);

            return ResponseEntity.ok(richiesta);
        } catch (Exception e) {
            // Gestione errori
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);

        }
    }

    @PostMapping(value = "/curatore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RichiestaCollaborazione> creaRichiestaCuratore(
            @RequestParam String nome,
            @RequestParam String cognome,
            @RequestParam String telefono,
            @RequestParam String email,
            @RequestParam Ruolo ruolo,
            @RequestParam String iban,
            @RequestPart("cartaIdentita") MultipartFile cartaIdentita,
            @RequestPart("cv") MultipartFile cv) {

        try {
            // Converti i MultipartFile in File
            File fileCartaIdentita = convertiMultipartFileToFile(cartaIdentita);
            File fileCv = convertiMultipartFileToFile(cv);

            // Chiama il servizio con i parametri e i file convertiti
            RichiestaCollaborazione richiesta = richiesteCollaborazioneService.creaRichiestaCuratore(
                    nome, cognome, telefono, email, ruolo, iban, fileCartaIdentita, fileCv);

            return ResponseEntity.ok(richiesta);
        } catch (Exception e) {
            // Gestione errori
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PatchMapping("/{id}/stato")
    public ResponseEntity<RichiestaCollaborazione> updateStato(@PathVariable Long id, @RequestParam Boolean stato) {
        Optional<RichiestaCollaborazione> richiesta = richiesteCollaborazioneService.getRichiestaById(id);
        if (richiesta.isPresent()) {
            RichiestaCollaborazione updatedRichiesta = richiesteCollaborazioneService.setStato(id, stato);
            return ResponseEntity.ok(updatedRichiesta);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/genera_account")
    public String generaAccount() {
        // TODO implementare
        return null;
    }

    private File convertiMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        // Creazione di un file temporaneo
        File file = File.createTempFile("file-", multipartFile.getOriginalFilename());

        // Copia il contenuto del MultipartFile nel file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        // Ritorna il file
        return file;
    }

}