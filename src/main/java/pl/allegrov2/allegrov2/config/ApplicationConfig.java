package pl.allegrov2.allegrov2.config;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.allegrov2.allegrov2.validation.exceptions.NotFoundException;
import pl.allegrov2.allegrov2.validation.exceptions.UnauthorizedException;
import pl.allegrov2.allegrov2.repositories.UserRepository;

@Configuration
@AllArgsConstructor
public class ApplicationConfig {

    private UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("User with email: " + username + " not found."));
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());

        authProvider.setPreAuthenticationChecks(userDetails -> {
            if (!userDetails.isEnabled()) {
                throw new UnauthorizedException("User account is disabled. Maybe confirming the email is required.");
            }
        });

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public Faker faker(){
        return new Faker();
    }
}
