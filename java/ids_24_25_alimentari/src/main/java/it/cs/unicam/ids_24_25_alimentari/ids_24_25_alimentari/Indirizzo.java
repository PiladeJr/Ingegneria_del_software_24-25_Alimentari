package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;


    @Entity
    @Table(name = "Indirizzo")
    @NoArgsConstructor(force = true)
    public class Indirizzo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String citta;
    private String cap;
    private String via;
    private int numeroCivico;
    private String provincia;
    private String coordinate;

        public Indirizzo(String citta, String cap, String via, int numeroCivico, String provincia, String coordinate) {
            this.citta = citta;
            this.cap = cap;
            this.via = via;
            this.numeroCivico = numeroCivico;
            this.provincia = provincia;
            this.coordinate = coordinate;
        }
        public long getId() {
            return id;
        }
        public String getCitta() {
            return citta;
        }

        public void setCitta(String citta) {
            this.citta = citta;
        }

        public String getCap() {
            return cap;
        }

        public void setCap(String cap) {
            this.cap = cap;
        }

        public String getVia() {
            return via;
        }

        public void setVia(String via) {
            this.via = via;
        }

        public int getNumeroCivico() {
            return numeroCivico;
        }

        public void setNumeroCivico(int numeroCivico) {
            this.numeroCivico = numeroCivico;
        }

        public String getProvincia() {
            return provincia;
        }

        public void setProvincia(String provincia) {
            this.provincia = provincia;
        }

        public String getCoordinate() {
            return coordinate;
        }

        public void setCoordinate(String coordinate) {
            this.coordinate = coordinate;
        }
}
