package it.moneygement.controller;

import it.moneygement.exception.AuthenticationException;
import it.moneygement.model.User;
import it.moneygement.service.MoneygementService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField pwdField;

    @FXML
    private Label statusLabel;

    @FXML
    private Button submitButton;

    private MoneygementService service;

    /**
     * Questo metodo viene chiamato automaticamente da JavaFX subito dopo il caricamento del file FXML.
     * Serve per inizializzare le dipendenze, ma NON per gestire l'azione dell'utente.
     */
    @FXML
    public void initialize() {
        // Prendo l'istanza del service (il Singleton)
        this.service = MoneygementService.getInstance();
    }

    /**
     * Questo metodo deve essere collegato al bottone nel file FXML (onAction="#handleLogin").
     * Viene eseguito SOLO quando l'utente clicca.
     */
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String pwd = pwdField.getText();

        // Controllo campi vuoti
        if (email.isBlank()) {
            statusLabel.setText("L'email è obbligatoria!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        if (pwd.isBlank()) {
            statusLabel.setText("La password è obbligatoria!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            // Chiamo il service. Se fallisce, lancerà un'eccezione che catturiamo sotto.
            User user = service.login(email, pwd);

            // Se siamo qui, il login è andato bene
            statusLabel.setText("Login riuscito!");
            statusLabel.setStyle("-fx-text-fill: green;");

            // TODO: Qui chiameremo SceneManager per andare alla Dashboard

        } catch (AuthenticationException e) {
            // Catturiamo l'errore specifico (es. credenziali errate)
            statusLabel.setText(e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}