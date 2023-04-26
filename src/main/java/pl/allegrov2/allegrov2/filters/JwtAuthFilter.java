package pl.allegrov2.allegrov2.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.allegrov2.allegrov2.controllers.AuthController;
import pl.allegrov2.allegrov2.services.token.JwtService;
import pl.allegrov2.allegrov2.services.user.UserService;


import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// Class intercepts requests to check whether it is authenticated
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        if(jwtService.isTokenExpired(jwtService.extractTokenFromAuthHeader(authHeader)))
        {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired. Login at " +
                    linkTo(methodOn(AuthController.class).login(null)).withRel("Login"));

            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = jwtService.extractTokenFromAuthHeader(authHeader);
        final String username = jwtService.extractUsername(jwt);

        // If token contains username and is not authenticated yet, then authenticate
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // Get user from database
            UserDetails userDetails = userService.loadUserByUsername(username);
            if(jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
