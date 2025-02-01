package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.smtp;

import org.springframework.stereotype.Service;


public interface ServizioEmail {

    String inviaMail(DettagliEmail dettagli);

}
