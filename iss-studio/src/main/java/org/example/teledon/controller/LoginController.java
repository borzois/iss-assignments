package org.example.teledon.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.teledon.domain.User;
import org.example.teledon.service.MainService;


public class LoginController {
    private MainService service;
    private MainController mainController;
    private static final Logger logger = LogManager.getLogger();

    @FXML
    TextField emailField;

    @FXML
    PasswordField passwordField;

    @FXML
    public void initialize() {

    }

    public void initData(MainService service, MainController mainController) {
        this.service = service;
        this.mainController = mainController;
    }

    public void handleLogin() {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            return;
        }
        User result = service.login(emailField.getText(), passwordField.getText());
        if (result == null) {
            logger.warn("incorrect credentials supplied");

            Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect credentials", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        mainController.setUser(result);
        logger.info("logged in as " + result.getEmail());

        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Logged in", ButtonType.OK);
        alert.showAndWait();
    }
    public void handleRegister() {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            return;
        }
        User result = service.register(emailField.getText(), passwordField.getText());
        if (result == null) {
            logger.warn("user already exists");

            Alert alert = new Alert(Alert.AlertType.ERROR, "User already exists", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        mainController.setUser(result);
        logger.info("account created: " + result.getEmail());
        logger.info("logged in as " + result.getEmail());

        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account created", ButtonType.OK);
        alert.showAndWait();
    }

}
