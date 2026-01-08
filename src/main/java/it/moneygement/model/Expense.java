package it.moneygement.model;

import it.moneygement.exception.DatiNonValidiException;
import java.time.LocalDateTime;

/**
 * Rappresenta una singola Spesa all'interno del sistema.
 * Questa classe funge da modello dati per gestire le transazioni in uscita.
 *
 * @author Stefano Bellan
 */
public class Expense {

    // Attributi della classe dichiarati privati per garantire l'incapsulamento dei dati
    private String nomeSpesa;
    private Categories categoria;
    private String descrizione;
    private double importo;
    private LocalDateTime data;
    private int id;

    /**
     * Costruttore della classe Expense.
     * Inizializza una nuova istanza validando i dati attraverso i setter.
     *
     * @param nomeSpesa  il nome breve della spesa.
     * @param categoria  la categoria di appartenenza (da enumeratore).
     * @param descrizione una descrizione dettagliata della spesa.
     * @param importo    il valore monetario della spesa.
     * @param data       la data e l'ora in cui la spesa è stata effettuata.
     * @throws DatiNonValidiException se uno dei dati non rispetta i criteri di validazione.
     */
    public Expense(String nomeSpesa, Categories categoria, String descrizione, double importo, LocalDateTime data) {
        // Utilizziamo i metodi setter all'interno del costruttore per sfruttare la logica di validazione centralizzata
        this.setNomeSpesa(nomeSpesa);
        this.setCategoria(categoria);
        this.setDescrizione(descrizione);
        this.setImporto(importo);
        this.setData(data);
    }

    /**
     * Restituisce il nome della spesa.
     *
     * @return una stringa rappresentante il nome della spesa.
     */
    public String getNomeSpesa() {
        return nomeSpesa;
    }

    /**
     * Imposta il nome della spesa dopo aver effettuato la validazione.
     *
     * @param nomeSpesa il nuovo nome da assegnare.
     * @throws DatiNonValidiException se il nome è nullo o vuoto/composto solo da spazi.
     */
    public void setNomeSpesa(String nomeSpesa) {
        // Verifica se la stringa è nulla o vuota (es. " ")
        if (nomeSpesa == null || nomeSpesa.isBlank()) {
            // Solleva un'eccezione specifica per bloccare l'inserimento di dati non validi
            throw new DatiNonValidiException("Il nome della spesa non può essere vuoto!!!");
        }
        // Assegna il valore validato all'attributo della classe
        this.nomeSpesa = nomeSpesa;
    }

    /**
     * Restituisce la categoria della spesa.
     *
     * @return l'enumeratore corrispondente alla categoria.
     */
    public Categories getCategoria() {
        return categoria;
    }

    /**
     * Imposta la categoria della spesa.
     *
     * @param categoria la nuova categoria da assegnare.
     * @throws DatiNonValidiException se la categoria è nulla.
     */
    public void setCategoria(Categories categoria) {
        // Verifica che sia stata passata una categoria valida e non null
        if (categoria == null) {
            throw new DatiNonValidiException("La categoria non può non essere selezionata!!");
        }
        this.categoria = categoria;
    }

    /**
     * Restituisce la descrizione della spesa.
     *
     * @return una stringa con la descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione della spesa con validazione.
     *
     * @param descrizione la nuova descrizione.
     * @throws DatiNonValidiException se la descrizione è nulla o vuota.
     */
    public void setDescrizione(String descrizione) {
        // Controllo validità del testo della descrizione
        if (descrizione == null || descrizione.isBlank()) {
            throw new DatiNonValidiException("La descrizione non può essere vuota!!!");
        }
        this.descrizione = descrizione;
    }

    /**
     * Restituisce l'importo della spesa.
     *
     * @return un valore double rappresentante l'importo.
     */
    public double getImporto() {
        return importo;
    }

    /**
     * Imposta l'importo della spesa.
     *
     * @param importo il valore monetario.
     * @throws DatiNonValidiException se l'importo è minore o uguale a zero.
     */
    public void setImporto(double importo) {
        // L'importo deve essere logicamente positivo per una spesa
        if (importo <= 0) {
            throw new DatiNonValidiException("L'importo non può essere 0 o negativo");
        }
        this.importo = importo;
    }

    /**
     * Restituisce la data e l'ora della spesa.
     *
     * @return oggetto LocalDateTime della spesa.
     */
    public LocalDateTime getData() {
        return data;
    }

    /**
     * Imposta la data della spesa.
     *
     * @param data la data da assegnare.
     * @throws DatiNonValidiException se la data è nulla.
     */
    public void setData(LocalDateTime data) {
        // La data è obbligatoria
        if (data == null) {
            throw new DatiNonValidiException("La data non può essere nulla");
        }
        this.data = data;
    }

    /**
     * Restituisce l'identificativo univoco della spesa.
     *
     * @return intero rappresentante l'ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'ID della spesa. Solitamente utilizzato quando si recupera il dato dal database.
     *
     * @param id il nuovo identificativo.
     * @throws DatiNonValidiException se l'id è minore o uguale a zero.
     */
    public void setId(int id) {
        // Un ID di database è tipicamente un numero intero positivo
        if (id <= 0) {
            throw new DatiNonValidiException("L'id non può essere 0 o negativo");
        }
        this.id = id;
    }

    /**
     * Restituisce una rappresentazione testuale dell'oggetto Expense.
     * Utile per il debugging e i log.
     * * @return Stringa descrittiva dell'oggetto.
     */
    @Override
    public String toString() {
        return "Spesa{" +
                "id=" + id +
                ", nomeSpesa='" + nomeSpesa + '\'' +
                ", categoria=" + categoria +
                ", importo=" + importo +
                ", data=" + data +
                '}';
    }
}