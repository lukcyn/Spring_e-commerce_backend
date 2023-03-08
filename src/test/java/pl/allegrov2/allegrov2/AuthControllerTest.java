package pl.allegrov2.allegrov2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.allegrov2.allegrov2.controllers.AuthController;
import pl.allegrov2.allegrov2.data.dto.AddressDto;
import pl.allegrov2.allegrov2.data.dto.LoginDto;
import pl.allegrov2.allegrov2.data.dto.RegistrationDto;
import pl.allegrov2.allegrov2.data.dto.TokenDto;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.repositories.IUserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private AuthController controller;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private IUserRepository userRepository;

    private RegistrationDto registrationDto;



    @BeforeEach
    void init()
    {
        registrationDto = new RegistrationDto(
            "Name", "Surename", "Password", "Email", 123123123,
                new AddressDto("StreetName", 2, 2, "12-321")
        );
    }

    @Test
    void registerSuccessfully(){
        ResponseEntity<?> rp = controller.registerUser(registrationDto);
        assertEquals(HttpStatus.CREATED, rp.getStatusCode());
    }

    @Test
    void loginSuccessfully() {
        controller.registerUser(registrationDto);

        LoginDto loginDto = new LoginDto(registrationDto.getEmail(), registrationDto.getPassword());

        ResponseEntity<TokenDto> response = controller.login(loginDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void passwordMatchDatabaseEntity(){
        controller.registerUser(registrationDto);
        AppUser dbUser = userRepository.findByEmail(registrationDto.getEmail())
                        .orElseThrow(() -> new RuntimeException("User was not added to database"));

        assertTrue(encoder.matches(registrationDto.getPassword(), dbUser.getPassword()));
    }
}
