package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Constructor for dependency injection
    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Extract the Authorization header from the request
        final String authHeader = request.getHeader("Authorization");

        // Check if the header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // If not, continue the filter chain without authentication
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extract the JWT from the header
            final String jwt = authHeader.substring(7);
            // Get the username (email) from the JWT
            final String userEmail = jwtService.extractUsername(jwt);
            // Get the current authentication from the security context
            Authentication authentication = SecurityContextHolder
                    .getContext()
                    .getAuthentication();
            List<String> roles = jwtService.extractRoles(jwt);
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // If the userEmail is not null and there is no existing authentication
            if (userEmail != null && authentication == null) {
                // Load user details using the userDetailsService
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Validate the JWT token
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Create an authentication token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            authorities);

                    // Set the request details for the authentication token
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    // Set the authentication in the security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                } else {
                    // If the token is invalid, set the response status to 403
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }

            // Continue the filter chain
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            // Handle any exceptions by resolving them with the exception resolver
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}