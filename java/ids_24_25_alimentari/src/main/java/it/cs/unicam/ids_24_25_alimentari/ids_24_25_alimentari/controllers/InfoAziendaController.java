package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.InfoAziendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aziende/info")
public class InfoAziendaController {
    private final InfoAziendaService infoAziendaService;

    public InfoAziendaController(InfoAziendaService infoAziendaService) {
        this.infoAziendaService = infoAziendaService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getInfoById(@PathVariable long id) {
        return infoAziendaService.getInformazioniAggiuntive(id);
    }
}
