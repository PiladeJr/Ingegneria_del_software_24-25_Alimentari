package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils;

import java.io.File;

/**
 * classe con i metodi responsabili per il controllo delle credenziali
 * dell'account utente
 */

public class AccountManager {
    public static boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*[0-9].*");
    }

}
