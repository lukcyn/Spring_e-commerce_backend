package pl.allegrov2.allegrov2.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.allegrov2.allegrov2.services.token.JwtService;

import java.io.IOException;

//fixme add to security config after tests
@Component
@AllArgsConstructor
public class EnabledUserFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            final String username = jwtService.extractUsernameFromAuthHeader(authHeader);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!userDetails.isEnabled()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "User account is disabled. Maybe confirming the email is required.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

// io.jsonwebtoken.io.DecodingException: Illegal base64url character: ' '
//eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYW5kb21Vc2VyQHJhbmRvbS5jb20iLCJpYXQiOjE2ODEyMDU4NzgsImV4cCI6MTY4MTI5MjI3OH0.ixzPHk55OcEq5x61S83nN33CFMZ0ZaHMJ_4_7oOM8rc