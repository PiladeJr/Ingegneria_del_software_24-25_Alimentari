package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.azienda.Azienda;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.indirizzo.Indirizzo;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.contenuto.InformazioniAggiuntive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class AziendaTest {
    private Azienda azienda;

    @BeforeEach
    void setUp() {
        azienda = new Azienda();
        azienda.setId(1L);
        azienda.setDenominazioneSociale("Azienda Srl");
        azienda.setIva("IT12345678901");
    }

    @Test
    void testGettersAndSetters() {
        azienda.setDenominazioneSociale("Nuova Azienda Spa");
        assertEquals("Nuova Azienda Spa", azienda.getDenominazioneSociale());
        azienda.setIva("IT98765432109");
        assertEquals("IT98765432109", azienda.getIva());
        azienda.setId(2L);
        assertEquals(2L, azienda.getId());
    }

    @Test
    void testSedeLegaleOperativa() {
        Indirizzo sedeLegale = Mockito.mock(Indirizzo.class);
        Indirizzo sedeOperativa = Mockito.mock(Indirizzo.class);
        azienda.setSedeLegale(sedeLegale);
        azienda.setSedeOperativa(sedeOperativa);
        assertEquals(sedeLegale, azienda.getSedeLegale());
        assertEquals(sedeOperativa, azienda.getSedeOperativa());
    }

    @Test
    void testCertificato() {
        File certificato = Mockito.mock(File.class);
        azienda.setCertificato(certificato);
        assertEquals(certificato, azienda.getCertificato());
    }

    @Test
    void testInformazioniAggiuntive() {
        InformazioniAggiuntive info = Mockito.mock(InformazioniAggiuntive.class);
        azienda.setInformazioniAggiuntive(info);
        assertEquals(info, azienda.getInformazioniAggiuntive());
    }
}
