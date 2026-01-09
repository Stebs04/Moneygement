package it.moneygement.dao;
import java.sql.*;
import it.moneygement.model.User;
import it.moneygement.db.DbConnection;
import it.moneygement.exception.UserAlreadyExistException;

public class UserDAO {

    private final String INSERT_USER_QUERY = "INSERT INTO user (nome, cognome, email, password_hash, eta) VALUES" +
            "(?,?,?,?,?);";
    private final String LOGIN_QUERY = "SELECT FROM user WHERE id = ?";

    public void registerUser(User u) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connection conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(INSERT_USER_QUERY);

            // Binding dei parametri
            pstmt.setString(1, u.getNome());
            pstmt.setString(2, u.getCognome());
            pstmt.setString(3, u.getEmail());
            pstmt.setString(4, u.getPasswordHash());
            pstmt.setInt(5, u.getEta());

            // CORREZIONE QUI: Usa executeUpdate per INSERT/UPDATE/DELETE
            pstmt.executeUpdate();

            System.out.println("Utente " + u.getEmail() + " registrato con successo.");

        } catch (SQLException e) {
            // Gestione specifica per email duplicata
            if (e.getMessage().contains("UNIQUE")) {
                throw new UserAlreadyExistException("La mail è già presente nel sistema!!!");
            } else {
                // È utile stampare anche l'errore tecnico per capire cosa è successo
                System.err.println("Errore inserimento: " + e.getMessage());
            }
        } finally {
            // Pulizia risorse
            DatabaseHelper.close(pstmt, rs);
        }
    }

    public User login (int userId){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            Connection conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(LOGIN_QUERY);
            pstmt.setInt(1, userId);
        } catch (SQLException e){

        } finally {

        }
    }
}
