package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;


import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.RichiestaCollaborazioneAziendaDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.AziendaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.ConvertitoreMultipartFileToFile.convertiMultipartFileToFile;

@RestController
@RequestMapping("/api/azienda")
public class AziendaController {

    @Autowired
    AziendaService aziendaService = new AziendaService();

    @GetMapping
    public ResponseEntity<List<Azienda>> getAllAziende() {
        List<Azienda> aziende = aziendaService.getAllAziende();
        return ResponseEntity.ok(aziende);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Azienda> getAziendaById(@PathVariable Long id) {
        return aziendaService.getAziendaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Azienda> saveAzienda(@RequestBody Azienda azienda) {
        Azienda saveAzienda = aziendaService.saveAzienda(azienda);
        return ResponseEntity.ok(saveAzienda);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> deleteAzienda(@PathVariable Long id) {
        aziendaService.deleteAzienda(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/azienda", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Azienda> createAzienda(@RequestBody RichiestaCollaborazioneAziendaDTO collaborazione) {
        try {
            Azienda azienda = aziendaService.createAzienda(
                    collaborazione.getDenSociale(),
                    collaborazione.getSedeLegale(),
                    collaborazione.getSedeOperativa(),
                    collaborazione.getIva(),
                    collaborazione.getIban(),
                    convertiMultipartFileToFile(collaborazione.getCertificato())
            );
            return ResponseEntity.ok(azienda);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
