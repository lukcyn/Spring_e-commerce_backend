package pl.allegrov2.allegrov2.services.auth;

import pl.allegrov2.allegrov2.data.dto.LoginDto;
import pl.allegrov2.allegrov2.data.dto.RegistrationDto;
import pl.allegrov2.allegrov2.data.dto.TokenDto;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.ConfirmationToken;

public interface AuthService {

    /** Saves user in database and sends confirmation token.
     * The user will be unauthorized until email is confirmed*/
    ConfirmationToken signUpUser(AppUser user);

    /**
     * Saves user in database and sends confirmation token.
     * The user will be unauthorized until email is confirmed
     * */
    ConfirmationToken signUpUser(RegistrationDto user);

    /**
     * Returns a token required for user authorization
     * */
    TokenDto login(LoginDto user);
}
