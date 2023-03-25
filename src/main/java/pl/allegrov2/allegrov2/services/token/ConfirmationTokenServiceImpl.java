package pl.allegrov2.allegrov2.services.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.ConfirmationToken;
import pl.allegrov2.allegrov2.repositories.ConfirmationTokenRepository;
import pl.allegrov2.allegrov2.repositories.UserRepository;
import pl.allegrov2.allegrov2.validation.exceptions.UnauthorizedException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository tokenRepository;
    private final UserRepository userRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        tokenRepository.save(token);
    }

    public String generateToken(){
        return UUID.randomUUID().toString();
    }

    public ConfirmationToken generateToken(AppUser user){
        return new ConfirmationToken(
                generateToken(),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5),
                user
        );
    }

    public boolean confirmToken(String confirmationTokenUUID){
        Optional<ConfirmationToken> optToken = tokenRepository.findByToken(confirmationTokenUUID);

        if(optToken.isEmpty() || optToken.get().getExpiresAt().isBefore(LocalDateTime.now()))
            throw new UnauthorizedException("Invalid or expired token");

        ConfirmationToken token = optToken.get();
        AppUser user = token.getAppUser();

        user.setEnabled(true);
        userRepository.save(user);

        tokenRepository.delete(token);

        return true;
    }
}
