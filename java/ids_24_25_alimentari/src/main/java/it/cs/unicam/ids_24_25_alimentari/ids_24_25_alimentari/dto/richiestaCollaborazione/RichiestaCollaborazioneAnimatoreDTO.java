

package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto.richiestaCollaborazione;
import java.beans.ConstructorProperties;




import org.springframework.web.multipart.MultipartFile;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.modelli.utente.Ruolo;

public class RichiestaCollaborazioneAnimatoreDTO {

    private String nome;
    private String cognome;
    private String telefono;
    private String email;
    private Ruolo ruolo;
    private String aziendaReferente;
    private String iban;
    private MultipartFile cartaIdentita;

    @ConstructorProperties({ "nome", "cognome", "telefono", "email", "ruolo", "aziendaReferente", "iban",
            "cartaIdentita" })
    public RichiestaCollaborazioneAnimatoreDTO(String nome, String cognome, String telefono, String email, Ruolo ruolo,
            String aziendaReferente, String iban, MultipartFile cartaIdentita) {
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.email = email;
        this.ruolo = ruolo;
        this.aziendaReferente = aziendaReferente;
        this.iban = iban;
        this.cartaIdentita = cartaIdentita;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    public String getAziendaReferente() {
        return aziendaReferente;
    }

    public void setAziendaReferente(String aziendaReferente) {
        this.aziendaReferente = aziendaReferente;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public MultipartFile getCartaIdentita() {
        return cartaIdentita;
    }

    public void setCartaIdentita(MultipartFile cartaIdentita) {
        this.cartaIdentita = cartaIdentita;
    }
}
