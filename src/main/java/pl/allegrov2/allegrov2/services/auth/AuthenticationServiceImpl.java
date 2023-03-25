package pl.allegrov2.allegrov2.services.auth;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.allegrov2.allegrov2.data.dto.LoginDto;
import pl.allegrov2.allegrov2.data.dto.RegistrationDto;
import pl.allegrov2.allegrov2.data.dto.TokenDto;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.ConfirmationToken;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;
import pl.allegrov2.allegrov2.services.token.ConfirmationTokenServiceImpl;
import pl.allegrov2.allegrov2.services.token.JwtServiceImpl;
import pl.allegrov2.allegrov2.services.mapping.MappingServiceImpl;
import pl.allegrov2.allegrov2.services.email.EmailService;
import pl.allegrov2.allegrov2.repositories.UserRepository;
import pl.allegrov2.allegrov2.validation.exceptions.ResourceTakenException;
import pl.allegrov2.allegrov2.validation.exceptions.UnauthorizedException;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthService {

    @Value("${app.confirmation.link.prefix}")
    private String CONFIRMATION_LINK_PREFIX;

    private final UserRepository userRepository;
    private final ConfirmationTokenServiceImpl confirmationTokenService;
    private final JwtServiceImpl jwtTokenService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder;
    private final MappingServiceImpl mapper;

    public ConfirmationToken signUpUser(AppUser user){
        if(userRepository.findByEmail(user.getEmail()).isPresent())
            throw new ResourceTakenException("Email is taken");

        userRepository.save(user);

        ConfirmationToken confToken = confirmationTokenService.generateToken(user);
        confirmationTokenService.saveConfirmationToken(confToken);

        String link = CONFIRMATION_LINK_PREFIX + confToken.getToken();
        emailService.send(user.getEmail(), buildEmail(user.getName(), link),"Email confirmation");

        return confToken;
    }

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
