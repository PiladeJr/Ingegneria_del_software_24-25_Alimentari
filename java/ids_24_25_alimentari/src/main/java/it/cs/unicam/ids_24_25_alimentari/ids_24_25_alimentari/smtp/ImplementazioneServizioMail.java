package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.smtp;

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
    public String inviaMail(DettagliEmail dettagliEmail) {

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(dettagliEmail.getDestinatario());
            mailMessage.setText(dettagliEmail.getMessaggio());
            mailMessage.setSubject(dettagliEmail.getOggetto());

            javaMailSender.send(mailMessage);
            return "Mail Inviata Correttamente...";
        }

        catch (Exception e) {
            return "Errore durante l'invio della mail";
        }
    }
}
