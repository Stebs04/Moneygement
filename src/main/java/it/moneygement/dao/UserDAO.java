package it.moneygement.dao;

import it.moneygement.db.DbConnection;
import it.moneygement.exception.UserAlreadyExistException;
import it.moneygement.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe DAO (Data Access Object) per la gestione degli Utenti.
 * Implementa il pattern CRUD completo (Create, Read, Update, Delete).
 *
 * @author Stefano Bellan
 */
public class UserDAO {

    // --- QUERY SQL COSTANTI ---

    // INSERT: Salva un nuovo utente
    private final String INSERT_USER_QUERY = "INSERT INTO user (nome, cognome, email, password_hash, eta) VALUES (?, ?, ?, ?, ?);";

    // SELECT (Login): Cerca per email e password
    private final String LOGIN_QUERY = "SELECT * FROM user WHERE email = ? AND password_hash = ?;";

    // SELECT (By ID): Cerca un utente specifico tramite ID
    private final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM user WHERE id = ?;";

    // UPDATE: Aggiorna i dati di un utente esistente
    private final String UPDATE_USER_QUERY = "UPDATE user SET nome = ?, cognome = ?, email = ?, password_hash = ?, eta = ? WHERE id = ?;";

    // DELETE: Rimuove un utente dal sistema
    private final String DELETE_USER_QUERY = "DELETE FROM user WHERE id = ?;";


    // --- METODI CRUD ---

    /**
     * CREATE: Registra un nuovo utente nel database.
     *
     * @param u L'oggetto User da salvare.
     * @throws UserAlreadyExistException Se l'email è già in uso.
     */
    public void registerUser(User u) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connection conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(INSERT_USER_QUERY);

            pstmt.setString(1, u.getNome());
            pstmt.setString(2, u.getCognome());
            pstmt.setString(3, u.getEmail());
            pstmt.setString(4, u.getPasswordHash());
            pstmt.setInt(5, u.getEta());

            pstmt.executeUpdate();
            System.out.println("Utente registrato con successo: " + u.getEmail());

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                throw new UserAlreadyExistException("L'email inserita è già registrata nel sistema.");
            } else {
                System.err.println("Errore inserimento utente: " + e.getMessage());
            }
        } finally {
            DatabaseHelper.close(pstmt, rs);
        }
    }

    /**
     * READ: Effettua il login verificando le credenziali.
     *
     * @param email        L'email dell'utente.
     * @param passwordHash L'hash della password.
     * @return L'oggetto User loggato, oppure null se non trovato.
     */
    public User login(String email, String passwordHash) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connection conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(LOGIN_QUERY);

            pstmt.setString(1, email);
            pstmt.setString(2, passwordHash);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Ricostruzione dell'oggetto User dai dati del DB
                return mapRowToUser(rs);
            }

        } catch (SQLException e) {
            System.err.println("Errore login: " + e.getMessage());
        } finally {
            DatabaseHelper.close(pstmt, rs);
        }
        return null;
    }

    /**
     * READ: Recupera un utente tramite il suo ID univoco.
     *
     * @param id L'ID dell'utente da cercare.
     * @return L'oggetto User trovato, oppure null.
     */
    public User getUserById(int id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connection conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(SELECT_USER_BY_ID_QUERY);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToUser(rs);
            }

        } catch (SQLException e) {
            System.err.println("Errore recupero utente per ID: " + e.getMessage());
        } finally {
            DatabaseHelper.close(pstmt, rs);
        }
        return null;
    }

    /**
     * UPDATE: Aggiorna i dati di un utente esistente.
     *
     * @param u L'oggetto User con i dati aggiornati (e l'ID corretto).
     * @throws UserAlreadyExistException Se la nuova email è già usata da qualcun altro.
     */
    public void updateUser(User u) {
        PreparedStatement pstmt = null;

        try {
            Connection conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(UPDATE_USER_QUERY);

            // Attenzione all'ordine dei parametri nella query UPDATE
            pstmt.setString(1, u.getNome());
            pstmt.setString(2, u.getCognome());
            pstmt.setString(3, u.getEmail());
            pstmt.setString(4, u.getPasswordHash());
            pstmt.setInt(5, u.getEta());
            pstmt.setInt(6, u.getId()); // ID per la clausola WHERE

            pstmt.executeUpdate();
            System.out.println("Utente aggiornato con successo.");

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                throw new UserAlreadyExistException("La nuova email scelta è già utilizzata.");
            } else {
                System.err.println("Errore aggiornamento utente: " + e.getMessage());
            }
        } finally {
            DatabaseHelper.close(pstmt);
        }
    }

    /**
     * DELETE: Rimuove permanentemente un utente dal database.
     *
     * @param id L'ID dell'utente da cancellare.
     */
    public void deleteUser(int id) {
        PreparedStatement pstmt = null;

        try {
            Connection conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(DELETE_USER_QUERY);
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
            System.out.println("Utente con ID " + id + " eliminato.");

        } catch (SQLException e) {
            System.err.println("Errore cancellazione utente: " + e.getMessage());
        } finally {
            DatabaseHelper.close(pstmt);
        }
    }

    // --- METODO DI UTILITÀ PRIVATO ---

    /**
     * Metodo helper per convertire una riga del ResultSet in un oggetto User.
     * Evita di duplicare il codice di creazione dell'oggetto in 'login' e 'getUserById'.
     */
    private User mapRowToUser(ResultSet rs) throws SQLException {
        String nome = rs.getString("nome");
        String cognome = rs.getString("cognome");
        String email = rs.getString("email");
        String pwdHash = rs.getString("password_hash");
        int eta = rs.getInt("eta");
        int id = rs.getInt("id");

        User user = new User(nome, cognome, pwdHash, email, eta);
        user.setId(id);
        return user;
    }
}