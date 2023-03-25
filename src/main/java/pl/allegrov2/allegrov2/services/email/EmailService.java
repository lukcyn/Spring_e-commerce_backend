package pl.allegrov2.allegrov2.services.email;

public interface EmailService {
    void send(String to, String email, String subject);
}
