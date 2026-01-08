package it.moneygement.exception;

/**
 * Eccezione che viene lanciata se l'utente inserisce un'indirizzo mail già salvato nel sistema
 */
public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String message){
        super(message);
    }
}
