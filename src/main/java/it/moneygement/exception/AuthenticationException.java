package it.moneygement.exception;

/**
 * Eccezione che viene lanciata se l'utente sbaglia password o se l'utente non esiste
 * @author Stefano Bellan
 */
public class AuthenticationException extends RuntimeException{
    public AuthenticationException(String message)
    {
        super(message);
    }
}
