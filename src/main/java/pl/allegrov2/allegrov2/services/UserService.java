package pl.allegrov2.allegrov2.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.allegrov2.allegrov2.data.dto.user.LoginDto;
import pl.allegrov2.allegrov2.data.dto.user.RegistrationDto;
import pl.allegrov2.allegrov2.data.dto.user.TokenDto;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.ConfirmationToken;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;
import pl.allegrov2.allegrov2.interfaces.IEmailSender;
import pl.allegrov2.allegrov2.repositories.IUserRepository;
import pl.allegrov2.allegrov2.validation.exceptions.ResourceTakenException;
import pl.allegrov2.allegrov2.validation.exceptions.UnauthorizedException;

@Service
@AllArgsConstructor
public class UserService {

    private static final String CONFIRMATION_LINK_PREFIX =
            "http://localhost:8080/api/auth/confirm-email?token=";
    // TODO: may change it with some app constant

    private final IUserRepository userRepository;

    private final ConfirmationTokenService confirmationTokenService;
    private final JwtService jwtTokenService;
    private final IEmailSender emailService;

    private final BCryptPasswordEncoder encoder;
    private final MappingService mapper;

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

    public ConfirmationToken signUpUser(RegistrationDto user){
        return signUpUser(mapper.convertToEntity(
                            user,
                            encoder.encode(user.getPassword()),
                            AppUserRole.USER,
                            false,
                            false
        ));
    }

    public TokenDto login(LoginDto user){
        AppUser appUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Bad username or password"));

        if(!encoder.matches(user.getPassword(), appUser.getPassword()))
            throw new UnauthorizedException("Bad username or password");

        return (mapper.convertToDto(appUser, jwtTokenService.generateToken(appUser)));
    }


    private String buildEmail(String name, String link){
        return "Cześć " + name + "!\n"
                +"Kliknij w link, aby aktywować konto: " + link;
    }
}
