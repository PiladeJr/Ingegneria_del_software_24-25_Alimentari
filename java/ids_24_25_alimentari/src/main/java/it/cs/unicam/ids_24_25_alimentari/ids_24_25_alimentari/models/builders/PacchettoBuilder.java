package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.contenuto.prodotto.Pacchetto;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.contenuto.prodotto.ProdottoSingolo;

import java.util.HashSet;
import java.util.Set;

public class PacchettoBuilder {

    private Pacchetto pacchetto;

    public PacchettoBuilder() {
        this.pacchetto = new Pacchetto();
    }

    public void costruisciNome(String nome) {
        pacchetto.setNome(nome);
    }

    public void costruisciDescrizione(String descrizione) {
        pacchetto.setDescrizione(descrizione);
    }

    public void costruisciPrezzo(double prezzo) {
        pacchetto.setPrezzo(prezzo);
    }

    public void aggiungiProdotto(ProdottoSingolo prodotto){
        if(this.pacchetto.getProdotti() == null){
            Set<ProdottoSingolo> listaProdotti = new HashSet<ProdottoSingolo>();
            listaProdotti.add(prodotto);
            this.pacchetto.setProdotti(listaProdotti);
        }else
            this.pacchetto.aggiungiProdotto(prodotto);
    }

    public void reset(){
        this.pacchetto = new Pacchetto();
    }

    public Pacchetto getPacchetto() {
        Pacchetto pacchettoOttenuto = this.pacchetto;
        this.reset();
        return pacchettoOttenuto;
    }
}
