package pl.allegrov2.allegrov2.services;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.allegrov2.allegrov2.interfaces.IEmailSender;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailService implements IEmailSender {

    private static final String ADDRESS_FROM = "randomemailhopeitworks64531@gmail.com";



    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void send(String to, String email, String subject) {
//        try{
//            MimeMessage mimeMessage = createEmail(to, email, subject);
//            Message message = createMessageWithEmail(mimeMessage);
//            send(mimeMessage, message);
//        } catch (MessagingException | IOException e) {
//            LOGGER.error("Could not send email");
//            LOGGER.error(e.toString());
//        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(email);
        mailSender.send(message);
    }
          // TODO: ideally should use queue to retry sending emails
}
