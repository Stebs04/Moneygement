package it.moneygement.utils;

import it.moneygement.model.User;

/**
 * Gestisce la sessione dell'utente corrente nell'applicazione.
 * <p>
 * Questa classe implementa il pattern <b>Singleton</b> per garantire che esista
 * una sola istanza della sessione attiva in tutto il programma.
 * Serve a mantenere in memoria i dati dell'utente loggato (es. ID, nome)
 * per poterli utilizzare nelle varie schermate o operazioni (es. salvare una spesa).
 * </p>
 *
 * @author Stefano Bellan
 */
public class UserSession {

    // L'unica istanza statica della classe, condivisa da tutta l'applicazione (Singleton)
    private static UserSession instance;

    // L'oggetto User che rappresenta l'utente attualmente loggato
    // Se è null, significa che nessuno ha ancora fatto il login
    private User user;

    /**
     * Costruttore privato.
     * <p>
     * È fondamentale per il pattern Singleton: impedisce a qualsiasi altra classe
     * esterna di creare nuove istanze di UserSession scrivendo 'new UserSession()'.
     * L'unico modo per ottenere l'oggetto è chiamare getInstance().
     * </p>
     */
    private UserSession() {}

    /**
     * Restituisce l'unica istanza disponibile di UserSession.
     * <p>
     * Utilizza la tecnica del "Lazy Loading" (Caricamento Pigro): l'istanza viene creata
     * solo la prima volta che questo metodo viene chiamato, risparmiando risorse se non serve.
     * </p>
     *
     * @return L'istanza singleton di UserSession.
     */
    public static UserSession getInstance() {
        // Controlliamo se l'istanza è già stata creata in precedenza
        if (instance == null) {
            // Se non esiste, la creiamo ora per la prima volta
            instance = new UserSession();
        }
        // Restituiamo l'istanza (o quella appena creata o quella già esistente)
        return instance;
    }

    /**
     * Recupera l'utente attualmente loggato nella sessione.
     *
     * @return L'oggetto User loggato, oppure null se nessun utente è loggato.
     */
    public User getUser() {
        return user;
    }

    /**
     * Imposta l'utente loggato nella sessione corrente.
     * Da chiamare tipicamente nel Service dopo che il login su Database ha avuto successo.
     *
     * @param user L'oggetto User da salvare in sessione.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Pulisce la sessione corrente rimuovendo l'utente.
     * <p>
     * Questo metodo deve essere chiamato quando l'utente effettua il <b>Logout</b>,
     * per garantire che i dati sensibili vengano rimossi dalla memoria e che
     * l'applicazione torni allo stato iniziale.
     * </p>
     */
    public void clearSession() {
        // Rimuoviamo il riferimento all'oggetto utente, rendendolo null
        this.user = null;
    }
}