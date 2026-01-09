package it.moneygement.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce la connessione al database SQLite dell'applicazione.
 * Implementa il design pattern Singleton per garantire l'esistenza di una sola
 * istanza di connessione attiva durante l'esecuzione del programma.
 *
 * @author Stefano Bellan
 */
public class DbConnection {

    // Variabile statica per memorizzare l'unica istanza della classe (Singleton)
    private static DbConnection instance;

    // Oggetto Connection che gestisce il collegamento fisico al database
    private Connection conn;

    // Stringa contenente il percorso di connessione JDBC (modificabile per i test)
    private static String dbPath = "jdbc:sqlite:moneygement.db";

    /**
     * Costruttore privato della classe.
     * Viene invocato solo internamente per inizializzare la connessione.
     */
    private DbConnection() {
        try {
            // Tenta di stabilire una connessione utilizzando il driver JDBC e il percorso specificato
            conn = DriverManager.getConnection(dbPath);
            // Stampa un messaggio di conferma sulla console
            System.out.println("Connessione al database stabilita con successo.");
        } catch (SQLException e) {
            // Intercetta e stampa eventuali errori di connessione (es. percorso errato, driver mancante)
            System.err.println("Errore di connessione al database: " + e.getMessage());
        }
    }

    /**
     * Restituisce l'istanza unica della classe DbConnection.
     * Utilizza la tecnica del "Lazy Loading": l'istanza viene creata solo
     * la prima volta che questo metodo viene chiamato.
     *
     * @return L'istanza singleton di DbConnection.
     */
    public static DbConnection getInstance() {
        // Verifica se l'istanza non è ancora stata creata
        if (instance == null) {
            // Crea la nuova istanza (che a sua volta apre la connessione nel costruttore)
            instance = new DbConnection();
        }
        // Restituisce l'istanza esistente (o quella appena creata)
        return instance;
    }

    /**
     * Restituisce l'oggetto Connection nativo di Java SQL.
     * Questo metodo permette alle altre classi (es. DAO) di eseguire query sul database.
     *
     * @return L'oggetto Connection attivo.
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Imposta un percorso del database alternativo (es. per i test unitari o di integrazione).
     * Questo metodo deve essere chiamato prima di ottenere l'istanza o per forzare un cambio di database.
     *
     * @param testPath Il percorso JDBC del database di test (es. "jdbc:sqlite::memory:").
     */
    public static void setTestDatabase(String testPath) {
        // Aggiorna il percorso del database
        dbPath = testPath;
        // Resetta l'istanza singleton a null.
        // Questo forza il metodo getInstance() a creare un nuovo oggetto DbConnection
        // (e quindi una nuova connessione) alla prossima chiamata, utilizzando il nuovo percorso.
        instance = null;
    }
}