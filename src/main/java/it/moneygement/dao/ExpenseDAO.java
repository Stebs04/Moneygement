package it.moneygement.dao;

import it.moneygement.db.DbConnection;
import it.moneygement.exception.DatabaseException;
import it.moneygement.model.Categories;
import it.moneygement.model.Expense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per la gestione delle Spese (Expense).
 * Permette di creare, leggere, aggiornare ed eliminare spese collegate a uno specifico utente.
 *
 * @author Stefano Bellan
 */
public class ExpenseDAO {

    // --- QUERY SQL ---

    // INSERT: Nota che inseriamo anche 'user_id' per collegare la spesa all'utente
    private final String INSERT_EXPENSE = "INSERT INTO expense (nome_spesa, categoria, descrizione, importo, data, user_id) VALUES (?, ?, ?, ?, ?, ?);";

    // SELECT: Recupera tutte le spese DI UN CERTO UTENTE (WHERE user_id = ?)
    private final String SELECT_BY_USER = "SELECT * FROM expense WHERE user_id = ?;";

    // UPDATE: Aggiorna una spesa esistente (identificata dal suo id)
    private final String UPDATE_EXPENSE = "UPDATE expense SET nome_spesa = ?, categoria = ?, descrizione = ?, importo = ?, data = ? WHERE id = ?;";

    // DELETE: Cancella una spesa specifica
    private final String DELETE_EXPENSE = "DELETE FROM expense WHERE id = ?;";

    // SEARCH BY CATEGORY: Cerca una spesa in base alla categoria
    private final String SEARCH_BY_CATEGORY = "SELECT * FROM expense WHERE user_id = ? AND categoria = ?;";


    // --- METODI CRUD ---

    /**
     * CREATE: Salva una nuova spesa nel database.
     *
     * @param e La spesa da salvare (deve avere l'idUtente impostato!).
     */
    public void addExpense(Expense e) {
        PreparedStatement pstmt = null;
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(INSERT_EXPENSE);

            // 1. Nome
            pstmt.setString(1, e.getNomeSpesa());
            // 2. Categoria (Salviamo la stringa dell'enum, es. "CIBO")
            pstmt.setString(2, e.getCategoria().name());
            // 3. Descrizione
            pstmt.setString(3, e.getDescrizione());
            // 4. Importo
            pstmt.setDouble(4, e.getImporto());
            // 5. Data (La convertiamo in stringa ISO-8601 standard)
            pstmt.setString(5, e.getData().toString());
            // 6. ID Utente (Fondamentale!)
            pstmt.setInt(6, e.idUtente());

            pstmt.executeUpdate();
            System.out.println("Spesa salvata: " + e.getNomeSpesa());

        } catch (SQLException ex) {
            System.err.println("Errore salvataggio spesa: " + ex.getMessage());
        } finally {
            DatabaseHelper.close(pstmt);
        }
    }

    /**
     * READ: Ottiene la lista di tutte le spese di un determinato utente.
     *
     * @param userId L'ID dell'utente di cui vogliamo le spese.
     * @return Una lista (ArrayList) di oggetti Expense.
     */
    public List<Expense> getExpensesByUserId(int userId) {
        List<Expense> expenses = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connection conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(SELECT_BY_USER);
            pstmt.setInt(1, userId);

            rs = pstmt.executeQuery();

            // Cicliamo su tutte le righe trovate
            while (rs.next()) {
                expenses.add(mapRowToExpense(rs));
            }

        } catch (SQLException ex) {
            System.err.println("Errore recupero spese: " + ex.getMessage());
        } finally {
            DatabaseHelper.close(pstmt, rs);
        }
        return expenses;
    }

    /**
     * UPDATE: Aggiorna i dettagli di una spesa esistente.
     *
     * @param e L'oggetto Expense con i dati aggiornati.
     */
    public void updateExpense(Expense e) {
        PreparedStatement pstmt = null;
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(UPDATE_EXPENSE);

            pstmt.setString(1, e.getNomeSpesa());
            pstmt.setString(2, e.getCategoria().name());
            pstmt.setString(3, e.getDescrizione());
            pstmt.setDouble(4, e.getImporto());
            pstmt.setString(5, e.getData().toString());
            // L'ultimo parametro è l'ID della spesa per il WHERE
            pstmt.setInt(6, e.getId());

            pstmt.executeUpdate();
            System.out.println("Spesa aggiornata: " + e.getNomeSpesa());

        } catch (SQLException ex) {
            System.err.println("Errore aggiornamento spesa: " + ex.getMessage());
        } finally {
            DatabaseHelper.close(pstmt);
        }
    }

    /**
     * DELETE: Cancella una spesa dal database.
     *
     * @param expenseId L'ID della spesa da eliminare.
     */
    public void deleteExpense(int expenseId) {
        PreparedStatement pstmt = null;
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(DELETE_EXPENSE);
            pstmt.setInt(1, expenseId);

            pstmt.executeUpdate();
            System.out.println("Spesa eliminata ID: " + expenseId);

        } catch (SQLException ex) {
            System.err.println("Errore cancellazione spesa: " + ex.getMessage());
        } finally {
            DatabaseHelper.close(pstmt);
        }
    }

    /**
     * Cerca tutte le spese di una specifica categoria per un determinato utente.
     * * @param userId L'ID dell'utente che effettua la ricerca.
     * @param categoria La categoria da cercare (usiamo l'Enum per sicurezza).
     * @return Una lista di spese trovate.
     */
    public List<Expense> searchByCategory(int userId, Categories categoria) {
        List<Expense> spese = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connection conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(SEARCH_BY_CATEGORY);

            // Impostiamo i parametri rispettando l'ordine della query
            pstmt.setInt(1, userId);
            pstmt.setString(2, categoria.name()); // Convertiamo l'Enum in Stringa per il DB

            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Riutilizziamo il metodo helper che abbiamo già scritto
                Expense spesa = mapRowToExpense(rs);
                spese.add(spesa);
            }

        } catch (SQLException e) {
            System.err.println("Errore nella ricerca per categoria: " + e.getMessage());
        } finally {
            DatabaseHelper.close(pstmt, rs);
        }

        // 3. RETURN CORRETTO: Restituisce la lista completa alla fine
        return spese;
    }

    // --- HELPER PRIVATO ---

    /**
     * Converte una riga del database in un oggetto Expense Java.
     * Gestisce la conversione complessa di Date ed Enum.
     */
    private Expense mapRowToExpense(ResultSet rs) throws SQLException {
        // Recuperiamo i dati grezzi
        int id = rs.getInt("id");
        String nome = rs.getString("nome_spesa");
        String catString = rs.getString("categoria");
        String desc = rs.getString("descrizione");
        double importo = rs.getDouble("importo");
        String dateString = rs.getString("data");
        int userId = rs.getInt("user_id");

        // Conversioni:
        // 1. String -> LocalDateTime
        LocalDateTime data = LocalDateTime.parse(dateString);
        // 2. String -> Enum Categories
        Categories cat = Categories.valueOf(catString);

        // Creazione Oggetto
        Expense e = new Expense(nome, cat, desc, importo, data);
        e.setId(id);
        e.setIdUtente(userId);

        return e;
    }
}