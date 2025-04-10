package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.builders;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.models.contenuto.prodotto.ProdottoSingolo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProdottoBuilder {
    private ProdottoSingolo prodotto;

    public ProdottoBuilder() {
        prodotto = new ProdottoSingolo();
    }

    public void costruisciNome(String nome) {
        prodotto.setNome(nome);
    }

    public void costruisciDescrizione(String descrizione) {
        prodotto.setDescrizione(descrizione);
    }

    public void costruisciIdAzienda(Long idAzienda) {
        prodotto.setIdAzienda(idAzienda);
    }

    public void aggiungiImmagine(File immagine) {
        if(this.prodotto.getImmagini() == null){
            List<File> immagini = new ArrayList<>();
            immagini.add(immagine);
            this.prodotto.setImmagini(immagini);
        }else{
            this.prodotto.getImmagini().add(immagine);
        }
    }

    public void costruisciPrezzo(double prezzo) {
        prodotto.setPrezzo(prezzo);
    }

    public void costruisciQuantita(int quantita) {
        prodotto.setQuantita(quantita);
    }

    public void costruisciAllergeni(String allergeni) {
        prodotto.setAllergeni(allergeni);
    }

    public void costruisciTecniche(String tecniche) {
        prodotto.setTecniche(tecniche);
    }

    public ProdottoSingolo getProdotto() {
        ProdottoSingolo prodottoOttenuto = this.prodotto;
        reset();
        return prodottoOttenuto;

    }

    public void reset() {
        prodotto = new ProdottoSingolo();
    }

}
