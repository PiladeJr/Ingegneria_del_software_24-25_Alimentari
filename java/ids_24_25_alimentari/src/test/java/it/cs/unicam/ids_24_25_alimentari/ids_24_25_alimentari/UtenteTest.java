package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Utente;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import java.io.File;

public class UtenteTest {
    private Utente utente;

    @BeforeEach
    void setUp() {
        utente = new Utente("Mario", "Rossi", "mario.rossi@email.com", "password123", "1234567890");
        utente.setRuolo(Ruolo.ACQUIRENTE);
        utente.setIban("IT60X0542811101000000123456");
    }

    @Test
    void testGettersAndSetters() {
        utente.setNome("Luigi");
        assertEquals("Luigi", utente.getNome());
        utente.setCognome("Verdi");
        assertEquals("Verdi", utente.getCognome());
        utente.setEmail("luigi.verdi@email.com");
        assertEquals("luigi.verdi@email.com", utente.getEmail());
        utente.setTelefono("0987654321");
        assertEquals("0987654321", utente.getTelefono());
        utente.setIban("IT60X0542811101000000123457");
        assertEquals("IT60X0542811101000000123457", utente.getIban());
    }

    @Test
    void testRuolo() {
        utente.setRuolo(Ruolo.CURATORE);
        assertEquals(Ruolo.CURATORE, utente.getRuolo());
    }

    @Test
    void testIndirizzoFatturazione() {
        Indirizzo indirizzo = Mockito.mock(Indirizzo.class);
        utente.setIndirizzoFatturazione(indirizzo);
        assertEquals(indirizzo, utente.getIndirizzoFatturazione());
    }

    @Test
    void testIndirizzoSpedizione() {
        Indirizzo indirizzo = Mockito.mock(Indirizzo.class);
        utente.setIndirizzoSpedizione(indirizzo);
        assertEquals(indirizzo, utente.getIndirizzoSpedizione());
    }

    @Test
    void testCvAndCartaIdentita() {
        File cv = Mockito.mock(File.class);
        File carta = Mockito.mock(File.class);
        utente.setCv(cv);
        utente.setCartaIdentita(carta);
        assertEquals(cv, utente.getCv());
        assertEquals(carta, utente.getCartaIdentita());
    }

    @Test
    void testIdAzienda() {
        utente.setIdAzienda(123L);
        assertEquals(123L, utente.getIdAzienda());
    }

    @Test
    void testAuthorities() {
        utente.setRuolo(Ruolo.GESTORE);
        assertTrue(utente.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GESTORE")));
    }

    @Test
    void testUsernameAndPassword() {
        assertEquals(utente.getEmail(), utente.getUsername());
        assertNotNull(utente.getPassword());
    }
}
