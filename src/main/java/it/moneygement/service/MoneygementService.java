package it.moneygement.service;

import it.moneygement.dao.ExpenseDAO;
import it.moneygement.dao.UserDAO;
import it.moneygement.exception.AuthenticationException;
import it.moneygement.exception.RisorsaNonTrovataException;
import it.moneygement.model.Categories;
import it.moneygement.model.Expense;
import it.moneygement.model.User;
import it.moneygement.utils.SecurityUtils;
import it.moneygement.utils.UserSession;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service Layer: Lo "Chef" dell'applicazione.
 * Coordina le operazioni tra l'interfaccia grafica (Controller) e il database (DAO).
 * Gestisce la logica di business come l'hashing delle password e la validazione.
 *
 * @author Stefano Bellan
 */
public class MoneygementService {

    // --- VARIABILI DI ISTANZA ---
    private final UserDAO userDAO;
    private final ExpenseDAO expenseDAO;

    // --- SINGLETON ---
    // Variabile statica dello stesso tipo della classe
    private static MoneygementService instance;

    /**
     * Costruttore privato: Inizializza gli strumenti (DAO).
     * Essendo privato, impedisce l'istanziazione diretta dall'esterno.
     */
    private MoneygementService() {
        this.userDAO = new UserDAO();
        this.expenseDAO = new ExpenseDAO();
    }

    /**
     * Metodo pubblico statico per ottenere l'unica istanza (Singleton).
     * @return L'istanza condivisa di MoneygementService.
     */
    public static MoneygementService getInstance() {
        if (instance == null) {
            instance = new MoneygementService();
        }
        return instance;
    }

    // --- GESTIONE UTENTI ---

    /**
     * Registra un nuovo utente nel sistema.
     *
     * @param nome     Nome dell'utente.
     * @param cognome  Cognome dell'utente.
     * @param email    Email dell'utente.
     * @param password Password in chiaro (verrà hashata per sicurezza).
     * @param eta      Età dell'utente.
     */
    public void registerUser(String nome, String cognome, String email, String password, int eta) {
        // 1. Rendiamo sicura la password
        String passwordHash = SecurityUtils.hashPassword(password);

        // 2. Creiamo l'oggetto User
        User u = new User(nome, cognome, passwordHash, email, eta);

        // 3. Lo passiamo al  (DAO) per il salvataggio
        userDAO.registerUser(u);
    }

    /**
     * Effettua il login dell'utente verificando le credenziali.
     *
     * @param email    Email inserita.
     * @param password Password in chiaro inserita.
     * @return L'oggetto User loggato.
     * @throws AuthenticationException se le credenziali sono errate.
     */
    public User login(String email, String password) {
        String passwordHash = SecurityUtils.hashPassword(password);
        User userTrovato = userDAO.login(email, passwordHash);

        if (userTrovato == null) {
            throw new AuthenticationException("Email o Password non validi!!!");
        } else {
            // Salviamo l'utente nella sessione globale
            UserSession.getInstance().setUser(userTrovato);
        }
        return userTrovato;
    }

    /**
     * Aggiorna i dati dell'utente attualmente loggato.
     *
     * @param nome     Nuovo nome.
     * @param cognome  Nuovo cognome.
     * @param password Nuova password in chiaro.
     * @param email    Nuova email.
     * @param eta      Nuova età.
     */
    public void updateUser(String nome, String cognome, String password, String email, int eta) {
        String passwordHash = SecurityUtils.hashPassword(password);
        User utenteLoggato = UserSession.getInstance().getUser();

        // Aggiorniamo l'oggetto in memoria prima di passarlo al DAO
        utenteLoggato.setNome(nome);
        utenteLoggato.setCognome(cognome);
        utenteLoggato.setPasswordHash(passwordHash);
        utenteLoggato.setEmail(email);
        utenteLoggato.setEta(eta);

        userDAO.updateUser(utenteLoggato);
    }

    /**
     * Recupera l'utente loggato dal database tramite il suo ID.
     *
     * @return L'oggetto User trovato.
     * @throws RisorsaNonTrovataException se l'utente non esiste nel DB.
     */
    public User findUserById() throws RisorsaNonTrovataException {
        int idUtente = UserSession.getInstance().getUser().getId();
        User utenteTrovato = userDAO.getUserById(idUtente);

        if (utenteTrovato == null) {
            throw new RisorsaNonTrovataException("Utente con id: " + idUtente + " non trovato nel database!!");
        } else {
            return utenteTrovato;
        }
    }

    /**
     * Elimina l'account dell'utente attualmente loggato.
     */
    public void deleteUserById() { // CORREZIONE STILE: CamelCase (da DeleteUserbyId a deleteUserById)
        int idUtente = UserSession.getInstance().getUser().getId();
        userDAO.deleteUser(idUtente);
        UserSession.getInstance().clearSession();
    }

    // --- GESTIONE SPESE ---

    /**
     * Aggiunge una nuova spesa per l'utente loggato.
     *
     * @param nome    Nome spesa.
     * @param cat     Categoria.
     * @param desc    Descrizione.
     * @param importo Importo.
     * @param data    Data.
     */
    public void addExpense(String nome, Categories cat, String desc, double importo, LocalDateTime data) {
        User userLoggato = UserSession.getInstance().getUser();
        Expense spesa = new Expense(nome, cat, desc, importo, data);
        // Colleghiamo la spesa all'utente corrente
        spesa.setIdUtente(userLoggato.getId());

        expenseDAO.addExpense(spesa);
    }

    /**
     * Recupera la lista di tutte le spese dell'utente loggato.
     *
     * @return Una lista di oggetti Expense.
     * @throws RisorsaNonTrovataException se la lista è vuota.
     */
    public List<Expense> getExpensesByUserId() throws RisorsaNonTrovataException {
        int userId = UserSession.getInstance().getUser().getId();
        List<Expense> spese = expenseDAO.getExpensesByUserId(userId);

        // I DAO restituiscono tipicamente una lista vuota se non trovano nulla, non null.
        if (spese.isEmpty()) {
            throw new RisorsaNonTrovataException("Non è presente nessuna spesa associata a questo id: " + userId);
        }
        return spese;
    }

    /**
     * Aggiorna una spesa esistente.
     *
     * @param idSpesa ID univoco della spesa da modificare.
     * @param nome    Nuovo nome.
     * @param cat     Nuova categoria.
     * @param desc    Nuova descrizione.
     * @param importo Nuovo importo.
     * @param data    Nuova data.
     */
    public void updateExpense(int idSpesa, String nome, Categories cat, String desc, double importo, LocalDateTime data) { // CORREZIONE STILE: CamelCase e nome (da UpdateExpenses a updateExpense)
        User userLoggato = UserSession.getInstance().getUser();

        // Creiamo un nuovo oggetto Expense con i dati aggiornati
        Expense spesa = new Expense(nome, cat, desc, importo, data);
        spesa.setId(idSpesa);

        // Manteniamo il riferimento all'utente proprietario
        spesa.setIdUtente(userLoggato.getId());

        expenseDAO.updateExpense(spesa);
    }

    /**
     * Cancella una spesa specifica tramite il suo ID.
     *
     * @param idSpesa ID della spesa da eliminare.
     */
    public void deleteExpenseById(int idSpesa) { // CORREZIONE STILE: CamelCase (da DeleteExpensebyId a deleteExpenseById)
        expenseDAO.deleteExpense(idSpesa);
    }

    /**
     * Cerca le spese per una specifica categoria.
     *
     * @param cat La categoria da cercare.
     * @return Una lista di spese filtrate.
     * @throws RisorsaNonTrovataException se nessuna spesa corrisponde ai criteri.
     */
    public List<Expense> searchByCategory(Categories cat) throws RisorsaNonTrovataException {
        int userId = UserSession.getInstance().getUser().getId();
        List<Expense> spese = expenseDAO.searchByCategory(userId, cat);

        if (spese.isEmpty()) {
            throw new RisorsaNonTrovataException("Nessuna spesa per questa categoria: " + cat.name());
        }
        return spese;
    }
}