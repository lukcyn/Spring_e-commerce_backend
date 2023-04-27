package pl.allegrov2.allegrov2.services.email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void send(String to, String email, String subject) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(email);
        mailSender.send(message);
    }
}
