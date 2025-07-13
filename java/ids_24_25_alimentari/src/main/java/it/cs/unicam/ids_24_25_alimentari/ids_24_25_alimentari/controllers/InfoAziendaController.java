package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.InfoAziendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/aziende/info")
public class InfoAziendaController {
    private final InfoAziendaService infoAziendaService;

    public InfoAziendaController(InfoAziendaService infoAziendaService) {
        this.infoAziendaService = infoAziendaService;
    }
    /**
     * <h2>Recupera le informazioni aggiuntive di un'azienda</h2>
     * <br/>
     * <p>Endpoint per ottenere le informazioni aggiuntive associate a un'azienda.</p>
     *
     * @param id <i>ID</i> dell'azienda di cui recuperare le informazioni.
     * @return ResponseEntity contenente le informazioni aggiuntive o un errore.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getInfoById(@PathVariable long id) {
        return infoAziendaService.getInformazioniAggiuntive(id);
    }

    /**
     * <h2>Elimina virtualmente le informazioni aggiuntive</h2>
     * <br/>
     * <p>Endpoint per l'eliminazione virtuale delle informazioni aggiuntive.</p>
     * <p>Le informazioni vengono dissociate dall'azienda e marcate come "ELIMINATO".</p>
     *
     * @param id <i>ID</i> delle informazioni aggiuntive da eliminare.
     * @return ResponseEntity con un messaggio di successo o errore.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInfoAziendaVirtuale(@PathVariable Long id) {
        try {
            infoAziendaService.deleteInfoAziendaVirtuale(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Informazioni aggiuntive eliminate correttamente."));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Errore interno del server: " + e.getMessage()));
        }
    }
}
