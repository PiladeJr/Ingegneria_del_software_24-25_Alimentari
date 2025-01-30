package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.utente.Utente;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TokenConfig extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String authHeader = request.getHeader("Authorization");

            if(authHeader != null && authHeader.startsWith("Bearer ") && !authHeader.substring(7).isBlank()) {
                String accessToken = authHeader.substring(7);
                Utente user = isTokenValid(accessToken);

                if(user == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                else {
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRuolo().name()));
                    Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user.getId(), null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        }
    private Utente isTokenValid(String token) {
        OauthToken oauthToken = tokenRepository.findById(token).orElse(null);

        if(oauthToken == null || oauthToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            return null;
        }
        return oauthToken.getUser();
    }

}

