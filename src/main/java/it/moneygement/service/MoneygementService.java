package it.moneygement.service;

import it.moneygement.dao.ExpenseDAO;
import it.moneygement.dao.UserDAO;
import it.moneygement.exception.AuthenticationException;
import it.moneygement.exception.UserAlreadyExistException;
import it.moneygement.model.Categories;
import it.moneygement.model.Expense;
import it.moneygement.model.User;
import it.moneygement.utils.SecurityUtils;
import it.moneygement.utils.UserSession;

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
}