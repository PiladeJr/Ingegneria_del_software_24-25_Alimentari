package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.controllers.AuthenticationController;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.login.LoginResponseDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.login.LoginUserDto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.utente.UtenteRegistrazioneDTO;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.AutenticazioneService;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi.JwtService;
import org.springframework.http.HttpStatus;

public class AuthTest {
    @Mock
    private AutenticazioneService autenticazioneService;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationController = new AuthenticationController(jwtService, autenticazioneService);
    }

    @Test
    void testRegister() {
        UtenteRegistrazioneDTO dto = new UtenteRegistrazioneDTO();
        Utente utente = new Utente();
        Mockito.when(autenticazioneService.registrazione(dto)).thenReturn(utente);
        ResponseEntity<Utente> response = authenticationController.register(dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(utente, response.getBody());
    }

    @Test
    void testAuthenticate() {
        LoginUserDto loginDto = new LoginUserDto();
        Utente utente = new Utente();
        String token = "jwt-token";
        long expiresIn = 3600L;
        Mockito.when(autenticazioneService.authenticate(loginDto)).thenReturn(utente);
        Mockito.when(jwtService.generateToken(utente)).thenReturn(token);
        Mockito.when(jwtService.getExpirationTime()).thenReturn(expiresIn);
        ResponseEntity<LoginResponseDTO> response = authenticationController.authenticate(loginDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody().getToken());
        assertEquals(expiresIn, response.getBody().getExpiresIn());
    }
}
