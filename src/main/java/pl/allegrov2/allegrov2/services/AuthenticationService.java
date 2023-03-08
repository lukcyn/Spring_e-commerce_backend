package pl.allegrov2.allegrov2.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.allegrov2.allegrov2.data.dto.LoginDto;
import pl.allegrov2.allegrov2.data.dto.RegistrationDto;
import pl.allegrov2.allegrov2.data.dto.TokenDto;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.ConfirmationToken;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;
import pl.allegrov2.allegrov2.interfaces.IEmailSender;
import pl.allegrov2.allegrov2.repositories.IUserRepository;
import pl.allegrov2.allegrov2.validation.exceptions.ResourceTakenException;
import pl.allegrov2.allegrov2.validation.exceptions.UnauthorizedException;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private static final String CONFIRMATION_LINK_PREFIX =
            "http://localhost:8080/api/auth/confirm-email?token=";
    // TODO: may change it with some app constant

    private final IUserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final JwtService jwtTokenService;
    private final IEmailSender emailService;
    private final BCryptPasswordEncoder encoder;
    private final MappingService mapper;

    /** Saves user in database and sends confirmation token.
     * The user will be unauthorized until email is confirmed*/
    public ConfirmationToken signUpUser(AppUser user){
        //Check if user exists in database
        if(userRepository.findByEmail(user.getEmail()).isPresent())
            throw new ResourceTakenException("Email is taken");

        userRepository.save(user);

        ConfirmationToken confToken = confirmationTokenService.generateToken(user);
        confirmationTokenService.saveConfirmationToken(confToken);

        String link = CONFIRMATION_LINK_PREFIX + confToken.getToken();
        emailService.send(user.getEmail(), buildEmail(user.getName(), link),"Email confirmation");

        return confToken;
    }

    /**
     * Saves user in database and sends confirmation token.
     * The user will be unauthorized until email is confirmed
     * */
    public ConfirmationToken signUpUser(RegistrationDto user){
        return signUpUser(mapper.convertToEntity(
                user,
                encoder.encode(user.getPassword()),
                AppUserRole.USER,
                false,
                false
        ));
    }

    /**
     * Returns a token required for user authorization
     * */
    public TokenDto login(LoginDto user){
        AppUser appUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Bad username or password"));

        if(!encoder.matches(user.getPassword(), appUser.getPassword()))
            throw new UnauthorizedException("Bad username or password");

        return (mapper.convertToDto(appUser, jwtTokenService.generateToken(appUser)));
    }

    /**
     * Helper function that creates email content
     * */
    private String buildEmail(String name, String link){
        return "Cześć " + name + "!\n"
                +"Kliknij w link, aby aktywować konto: " + link;
    }
}
