package pl.allegrov2.allegrov2.helpers.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;
import pl.allegrov2.allegrov2.filters.EnabledUserFilter;
import pl.allegrov2.allegrov2.filters.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final EnabledUserFilter enabledUserFilter;

    private final AuthenticationProvider authenticationProvider;
    private final UserDetailsService userService;

    // todo filter enabled users!

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

                .requestMatchers("/api/user/**")
                    .authenticated()

                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(enabledUserFilter, JwtAuthFilter.class)
                .authenticationProvider(authenticationProvider)
                .csrf().disable();

        return http.build();
    }

//    @Bean
//    public FilterRegistrationBean<EnabledUserFilter> enabledUserFilterRegistrationBean(EnabledUserFilter enabledUserFilter) {
//        FilterRegistrationBean<EnabledUserFilter> registrationBean = new FilterRegistrationBean<>(enabledUserFilter);
//        registrationBean.setEnabled(false); // Disable the filter by default
//        return registrationBean;
//    }
}
