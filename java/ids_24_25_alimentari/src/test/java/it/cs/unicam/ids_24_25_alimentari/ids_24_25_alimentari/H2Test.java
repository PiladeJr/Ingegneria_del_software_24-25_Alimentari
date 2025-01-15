package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari;

import java.sql.Connection;
import java.sql.DriverManager;

public class H2Test {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:h2:file:/data/demo";
        String user = "sa";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to H2 database successfully!");
        } catch (Exception e) {
            System.err.println("Error connecting to H2 database: " + e.getMessage());
        }
    }
}
