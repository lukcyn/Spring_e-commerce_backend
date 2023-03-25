package pl.allegrov2.allegrov2.services.token;

import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.ConfirmationToken;

public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken token);

    String generateToken();

    ConfirmationToken generateToken(AppUser user);

    boolean confirmToken(String confirmationTokenUUID);
}
