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
//    public MimeMessage createEmail(String to, String text, String subject) throws MessagingException {
//
//        Properties prop = new Properties();
//        Session session = Session.getDefaultInstance(prop, null);
//        MimeMessage message = new MimeMessage(session);
//
//        message.setFrom(new InternetAddress("noreply@allegrov2.com"));
//        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
//        message.setSubject(subject);
//        message.setText(text);
//
//        return message;
//    }

//    private Message createMessageWithEmail(MimeMessage content) throws MessagingException, IOException {
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        content.writeTo(buffer);
//        byte[] bytes = buffer.toByteArray();
//
//        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
//        Message message = new Message();
//        message.setRaw(encodedEmail);
//
//        return message;
//    }
//
//    private void send(MimeMessage mimeMessage, Message message) throws IOException {
//        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
//                .createScoped(GmailScopes.GMAIL_SEND);
//
//        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
//
//        Gmail service = new Gmail.Builder(new NetHttpTransport(),
//                GsonFactory.getDefaultInstance(),
//                requestInitializer)
//                .setApplicationName("Gmail samples")
//                .build();
//
//            // Create send message
//            message = service.users().messages().send("me", message).execute();
//            LOGGER.error("Message id: " + message.getId());         //Todo remove after debuggind
//            LOGGER.error(message.toPrettyString());
//    }
}
