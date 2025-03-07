package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.smtp;


public interface ServizioEmail {

    String inviaMail(String emailDestinatario, String messaggio, String oggetto);

}
