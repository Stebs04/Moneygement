package it.moneygement.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe di utilità per la gestione delle risorse del database.
 * Si occupa di chiudere in modo sicuro le connessioni, gli statement e i result set
 * per evitare "Memory Leaks" (perdite di memoria) che potrebbero bloccare l'applicazione.
 *
 * @author Stefano Bellan
 */
public class DatabaseHelper {

    /**
     * Chiude un oggetto Statement (o PreparedStatement) in modo sicuro.
     * Gestisce internamente l'eccezione SQL per evitare di dover usare try-catch ovunque nel codice.
     *
     * @param stmt Lo statement da chiudere. Se è null, non fa nulla.
     */
    public static void close(Statement stmt) {
        try {
            // Controlla se l'oggetto esiste prima di provare a chiuderlo
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            // In caso di errore durante la chiusura, stampiamo l'errore ma non blocchiamo il flusso
            System.err.println("Errore nella chiusura dello Statement: " + e.getMessage());
        }
    }

    /**
     * Chiude un oggetto ResultSet in modo sicuro.
     * Sfrutta l'Overloading: stesso nome del metodo precedente, ma parametro diverso.
     *
     * @param rs Il ResultSet da chiudere. Se è null, non fa nulla.
     */
    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Errore nella chiusura del ResultSet: " + e.getMessage());
        }
    }

    /**
     * Metodo "Combo" per chiudere contemporaneamente Statement e ResultSet.
     * Molto utile nei blocchi 'finally' dei DAO per pulire tutto con una sola riga di codice.
     *
     * @param stmt Lo statement da chiudere.
     * @param rs   Il ResultSet da chiudere.
     */
    public static void close(Statement stmt, ResultSet rs) {
        // Riutilizza i metodi singoli scritti sopra
        close(stmt);
        close(rs);
    }
}