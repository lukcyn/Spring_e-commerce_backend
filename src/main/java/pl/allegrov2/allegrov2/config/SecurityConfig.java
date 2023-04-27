package pl.allegrov2.allegrov2.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;
import pl.allegrov2.allegrov2.filters.EnabledUserFilter;
import pl.allegrov2.allegrov2.filters.JwtAuthFilter;
import pl.allegrov2.allegrov2.services.user.UserService;
import pl.allegrov2.allegrov2.validation.exceptions.UnauthorizedException;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final EnabledUserFilter enabledUserFilter;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()

                .requestMatchers("/api/admin/**")
                .hasAuthority(AppUserRole.ADMIN.name())

                .requestMatchers("/api/auth/**")
                    .permitAll()

                .requestMatchers("/api/products/**")
                    .permitAll()

                .anyRequest()
                    .authenticated()

                .and()
                .addFilterBefore(enabledUserFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, EnabledUserFilter.class)
                .authenticationProvider(authenticationProvider())
                .csrf().disable();

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);

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
}
