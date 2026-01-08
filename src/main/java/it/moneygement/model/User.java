package it.moneygement.model;

import it.moneygement.exception.DatiNonValidiException;
import java.util.regex.Pattern;

/**
 * Rappresenta l'entità Utente all'interno del sistema Moneygement.
 * <p>
 * Questa classe funge da "Entity" e garantisce l'integrità dei dati tramite
 * rigorosi controlli di validazione. Nessun oggetto User può esistere in uno stato
 * invalido (es. email malformata o età inferiore ai limiti di legge).
 * </p>
 *
 * @author Stefano Bellan
 */
public class User {

    // Variabili di istanza per i dati anagrafici e di accesso
    private int id;
    private String nome;
    private String cognome;
    private String passwordHash;
    private String email;
    private int eta;

    // Pattern Regex precompilato per la verifica del formato email standard
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    /**
     * Costruisce un nuovo oggetto User validando immediatamente i dati forniti.
     * <p>
     * Questo costruttore delega l'impostazione dei campi ai rispettivi metodi setter,
     * assicurando che le regole di validazione vengano applicate fin dalla creazione.
     * </p>
     *
     * @param nome         Il nome dell'utente.
     * @param cognome      Il cognome dell'utente.
     * @param passwordHash La password dell'utente già sottoposta ad hashing (non in chiaro).
     * @param email        L'indirizzo email dell'utente.
     * @param eta          L'età dell'utente (deve essere >= 14).
     * @throws DatiNonValidiException Se uno qualsiasi dei parametri non soddisfa i requisiti di validità.
     */
    public User(String nome, String cognome, String passwordHash, String email, int eta) throws DatiNonValidiException {
        // Imposta il nome validandolo
        this.setNome(nome);
        // Imposta il cognome validandolo
        this.setCognome(cognome);
        // Imposta l'hash della password validandolo
        this.setPasswordHash(passwordHash);
        // Imposta l'email verificandone il formato
        this.setEmail(email);
        // Imposta l'età verificando i limiti minimi
        this.setEta(eta);
    }

    /**
     * Restituisce l'ID univoco dell'utente.
     *
     * @return L'identificativo numerico dell'utente.
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'ID univoco dell'utente.
     *
     * @param id Il nuovo identificativo (deve essere maggiore di 0).
     * @throws DatiNonValidiException Se l'ID è minore o uguale a zero.
     */
    public void setId(int id) {
        // Verifica che l'ID sia un numero positivo
        if (id <= 0) {
            throw new DatiNonValidiException("L'id non può essere 0 o negativo!!!");
        }
        this.id = id;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return Il nome dell'utente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'utente.
     *
     * @param nome Il nuovo nome (non può essere null o vuoto).
     * @throws DatiNonValidiException Se il nome è null o vuoto.
     */
    public void setNome(String nome) {
        // Controlla che la stringa contenga effettivamente dei caratteri
        if (nome == null || nome.isBlank()) {
            throw new DatiNonValidiException("Il nome non può essere un campo vuoto!!!");
        }
        this.nome = nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return Il cognome dell'utente.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome dell'utente.
     *
     * @param cognome Il nuovo cognome (non può essere null o vuoto).
     * @throws DatiNonValidiException Se il cognome è null o vuoto.
     */
    public void setCognome(String cognome) {
        // Controlla che la stringa contenga effettivamente dei caratteri
        if (cognome == null || cognome.isBlank()) {
            throw new DatiNonValidiException("Il cognome non può essere un campo vuoto!!!");
        }
        this.cognome = cognome;
    }

    /**
     * Restituisce l'hash della password.
     *
     * @return La stringa rappresentante l'hash della password.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Imposta l'hash della password.
     *
     * @param passwordHash Il nuovo hash (non può essere null o vuoto).
     * @throws DatiNonValidiException Se l'hash è mancante o vuoto.
     */
    public void setPasswordHash(String passwordHash) {
        // Verifica che l'hash sia presente e non sia una stringa vuota
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new DatiNonValidiException("La password (hash) è mancante.");
        }
        this.passwordHash = passwordHash;
    }

    /**
     * Restituisce l'indirizzo email dell'utente.
     *
     * @return L'email dell'utente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'indirizzo email dell'utente.
     *
     * @param email La nuova email (deve rispettare il formato standard).
     * @throws DatiNonValidiException Se l'email è null o non rispetta il formato regex.
     */
    public void setEmail(String email) {
        // Verifica il formato dell'email utilizzando l'espressione regolare definita nella costante
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new DatiNonValidiException("Formato email non valido (es. esempio@mail.com).");
        }
        this.email = email;
    }

    /**
     * Restituisce l'età dell'utente.
     *
     * @return L'età in anni.
     */
    public int getEta() {
        return eta;
    }

    /**
     * Imposta l'età dell'utente.
     *
     * @param eta La nuova età (deve essere >= 14 anni).
     * @throws DatiNonValidiException Se l'età è inferiore al limite consentito.
     */
    public void setEta(int eta) {
        // Applica la restrizione GDPR impedendo la registrazione a minori di 14 anni
        if (eta < 14) {
            throw new DatiNonValidiException("L'utente non può avere un'età inferiore ai 14 anni!!");
        }
        this.eta = eta;
    }
}