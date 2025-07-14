package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ImplementazioneServizioMail implements ServizioEmail{

    @Autowired private JavaMailSender javaMailSender;
    @Value("$MAIL_USERNAME") private String sender;


    /**
     * <h2>Invia una mail</h2>
     * <br>
     * Questo metodo consente di inviare una mail a un destinatario specifico.
     * Utilizza il servizio JavaMailSender per inviare il messaggio.
     *
     * @param emailDestinatario L'indirizzo email del destinatario.
     * @param messaggio         Il contenuto del messaggio da inviare.
     * @param oggetto           L'oggetto della mail.
     * @return {@code String} Messaggio di conferma o errore.
     */
    @Override
    public String inviaMail(String emailDestinatario, String messaggio, String oggetto) {

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDestinatario);
            mailMessage.setText(messaggio);
            mailMessage.setSubject(oggetto);

            javaMailSender.send(mailMessage);
            System.out.println();
            return "Mail Inviata Correttamente...";
        }

        catch (Exception e) {
            return "Errore durante l'invio della mail";
        }
    }
}
