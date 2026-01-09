package it.moneygement.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe responsabile dell'inizializzazione della struttura del database.
 * Si occupa di creare le tabelle necessarie se non esistono già.
 *
 * @author Stefano Bellan
 */
public class InitDatabase {

    // Query SQL per la creazione della tabella 'user'.
    // Definisce la struttura per memorizzare i dati anagrafici e di accesso.
    private final String createUserTable = "CREATE TABLE IF NOT EXISTS user (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nome TEXT NOT NULL, " +
            "cognome TEXT NOT NULL, " +
            "email TEXT UNIQUE NOT NULL, " +
            "password_hash TEXT NOT NULL, " +
            "eta INTEGER CHECK (eta >= 14));";

    // Query SQL per la creazione della tabella 'expense'.
    // Mappa i campi della classe Expense rendendo persistenti le spese.
    // Tutti i campi sono obbligatori (NOT NULL) per coerenza con il modello Java.
    private final String createExpenseTable = "CREATE TABLE IF NOT EXISTS expense (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nome_spesa TEXT NOT NULL, " +
            "categoria TEXT NOT NULL, " +
            "descrizione TEXT NOT NULL, " +
            "importo REAL NOT NULL, " +
            "data TEXT NOT NULL);";

    /**
     * Esegue la procedura di inizializzazione del database.
     * Recupera la connessione e invia i comandi SQL per creare le tabelle.
     */
    public void initializeDb() {
        try {
            // Ottiene l'istanza della connessione attiva tramite il Singleton
            Connection conn = DbConnection.getInstance().getConnection();

            // Crea un oggetto Statement per inviare comandi SQL al database
            Statement stmt = conn.createStatement();

            // Esegue la query per creare la tabella utenti
            stmt.executeUpdate(this.createUserTable);

            // Esegue la query per creare la tabella spese
            stmt.executeUpdate(this.createExpenseTable);

            // Conferma l'avvenuta operazione sulla console
            System.out.println("Tabelle del database create o verificate con successo.");

        } catch (SQLException e) {
            // Gestisce eventuali errori durante il processo di creazione (es. permessi, file bloccato)
            System.err.println("Errore nella creazione del database: " + e.getMessage());
        }
    }
}