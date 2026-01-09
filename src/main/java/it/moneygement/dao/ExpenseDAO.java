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

public class ExpenseDAO {
    // INSERT: Nota che inseriamo anche 'user_id' per collegare la spesa all'utente
    private final String INSERT_EXPENSE = "INSERT INTO expense (nome_spesa, categoria, descrizione, importo, data, user_id) VALUES (?, ?, ?, ?, ?, ?);";

    // SELECT: Recupera tutte le spese DI UN CERTO UTENTE (WHERE user_id = ?)
    private final String SELECT_BY_USER = "SELECT * FROM expense WHERE user_id = ?;";

    // UPDATE: Aggiorna una spesa esistente (identificata dal suo id)
    private final String UPDATE_EXPENSE = "UPDATE expense SET nome_spesa = ?, categoria = ?, descrizione = ?, importo = ?, data = ? WHERE id = ?;";

    // DELETE: Cancella una spesa specifica
    private final String DELETE_EXPENSE = "DELETE FROM expense WHERE id = ?;";

    // --- METODI CRUD ---

    public void addExpense(Expense e){
        PreparedStatement pstmt = null;
        try{

        }catch (SQLException e){
            System.err.println("Errore salvataggio spesa: " + ex.getMessage());
        }finally {

        }

    }
}
