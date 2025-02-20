package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.smtp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ImplementazioneServizioMail implements ServizioEmail{

    @Autowired private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}") private String sender;

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
