package it.moneygement.exception;

/**
 * Eccezione che viene lanciata quando i dati inseriti non sono validi
 * @author Stefano Bellan
 */
public class DatiNonValidiException extends IllegalArgumentException{
    public DatiNonValidiException(String message){
        super(message);
    }
}
