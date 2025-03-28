package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.smtp;

import org.springframework.stereotype.Service;

@Service
public interface ServizioEmail {

    String inviaMail(String emailDestinatario, String messaggio, String oggetto);

}
