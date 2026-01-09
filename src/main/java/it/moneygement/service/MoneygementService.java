package it.moneygement.service;

import it.moneygement.dao.ExpenseDAO;
import it.moneygement.dao.UserDAO;
import it.moneygement.exception.AuthenticationException;
import it.moneygement.exception.DatabaseException;
import it.moneygement.exception.RisorsaNonTrovataException;
import it.moneygement.exception.UserAlreadyExistException;
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
 */
public class MoneygementService {

    // --- VARIABILI DI ISTANZA (GLI STRUMENTI) ---
    private final UserDAO userDAO;
    private final ExpenseDAO expenseDAO;

    // --- SINGLETON (L'UNICO CHEF) ---
    // 1. Variabile statica dello stesso tipo della classe
    private static MoneygementService instance;

    // 2. Costruttore privato: Inizializza gli strumenti (DAO)
    private MoneygementService() {
        this.userDAO = new UserDAO();
        this.expenseDAO = new ExpenseDAO();
    }

    // 3. Metodo pubblico statico per ottenere l'unica istanza
    public static MoneygementService getInstance() {
        if (instance == null) {
            instance = new MoneygementService();
        }
        return instance;
    }

    public void registerUser(String nome, String cognome, String email, String password, int eta) {
        // 1. Rendiamo sicura la password
        String passwordHash = SecurityUtils.hashPassword(password);

        // 2. Creiamo l'oggetto
        User u = new User(nome, cognome, passwordHash, email, eta);

        // 3. Lo passiamo al magazziniere (DAO)
        userDAO.registerUser(u);
    }

    public User login(String email, String password){
        String passwordHash = SecurityUtils.hashPassword(password);
        User userTrovato = userDAO.login(email, passwordHash);
        if(userTrovato == null){
            throw new AuthenticationException("Email o Password non validi!!!");
        }
        else{
            UserSession.getInstance().setUser(userTrovato);
        }
        return userTrovato;
    }

    public void updateUser(String nome, String cognome, String password, String email, int eta){
        String passwordHash = SecurityUtils.hashPassword(password);
        User utenteLoggato = UserSession.getInstance().getUser();
        utenteLoggato.setNome(nome);
        utenteLoggato.setCognome(cognome);
        utenteLoggato.setPasswordHash(passwordHash);
        utenteLoggato.setEmail(email);
        utenteLoggato.setEta(eta);
        userDAO.updateUser(utenteLoggato);
    }

    public User findUserbyId() throws RisorsaNonTrovataException{
        int idUtente = UserSession.getInstance().getUser().getId();
        User utenteTrovato = userDAO.getUserById(idUtente);
        if(utenteTrovato == null){
            throw new RisorsaNonTrovataException("Utente con id: " + idUtente + "Non trovato nel database!!");
        }
        else{
            return utenteTrovato;
        }
    }

    public void DeleteUserbyId(){
        int idUtente = UserSession.getInstance().getUser().getId();
        userDAO.deleteUser(idUtente);
    }

    public void addExpense(String nome, Categories cat, String desc, double importo, LocalDateTime data){
        User userLoggato = UserSession.getInstance().getUser();
        Expense spesa = new Expense(nome, cat, desc, importo, data);
        spesa.setIdUtente(userLoggato.getId());
        expenseDAO.addExpense(spesa);
    }

    /**
     * Recupera le spese dell'utente.
     * CORREZIONE: Controlliamo se la lista è vuota, non se è null.
     */
    public List<Expense> getExpensesByUserId() throws RisorsaNonTrovataException {
        int userId = UserSession.getInstance().getUser().getId();
        List<Expense> spese = expenseDAO.getExpensesByUserId(userId);

        // È meglio usare isEmpty() perché i DAO di solito restituiscono liste vuote, non null
        if (spese.isEmpty()) {
            throw new RisorsaNonTrovataException("Non è presente nessuna spesa associata a questo id: " + userId);
        }
        return spese;
    }

    /**
     * Aggiorna una spesa esistente.
     * CORREZIONE: Aggiunto il parametro 'idSpesa' per sapere COSA aggiornare.
     */
    public void updateExpenses(int idSpesa, String nome, Categories cat, String desc, double importo, LocalDateTime data) {
        User userLoggato = UserSession.getInstance().getUser();

        // Creiamo l'oggetto con i nuovi dati
        Expense spesa = new Expense(nome, cat, desc, importo, data);

        // Impostiamo l'ID della spesa da modificare (FONDAMENTALE!)
        spesa.setId(idSpesa);
        // Impostiamo l'ID utente per sicurezza (anche se nel DAO non lo aggiorniamo)
        spesa.setIdUtente(userLoggato.getId());

        expenseDAO.updateExpense(spesa);
    }

    /**
     * Cancella una spesa.
     * CORREZIONE: Implementato il metodo che era vuoto.
     */
    public void deleteExpenseById(int idSpesa) {
        // Passiamo l'ordine al DAO
        expenseDAO.deleteExpense(idSpesa);
    }

    public List<Expense> searchByCategory(Categories cat) throws RisorsaNonTrovataException {
        int userId = UserSession.getInstance().getUser().getId();
        List<Expense> spese = expenseDAO.searchByCategory(userId, cat);

        if (spese.isEmpty()) {
            throw new RisorsaNonTrovataException("Nessuna spesa per questa categoria: " + cat.name());
        }
        return spese;
    }
}