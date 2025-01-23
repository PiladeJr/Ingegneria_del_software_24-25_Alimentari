package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.smtp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class EmailController {

    @Autowired private ServizioEmail servizioEmail;

    @PostMapping("/inviaMail")
    public ResponseEntity<DettagliEmail> inviaMail(@RequestBody DettagliEmail dettagliEmail) {
        servizioEmail.inviaMail(dettagliEmail);
        return ResponseEntity.ok(dettagliEmail);
    }


}
