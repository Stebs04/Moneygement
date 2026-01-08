package it.moneygement.exception;

import java.sql.SQLException;

/**
 * Impacchetta una SQLException che viene lanciata quando c'è un errore con il Database
 */
public class DatabaseException extends SQLException {
    public DatabaseException(String message){
        super(message);
    }
}
