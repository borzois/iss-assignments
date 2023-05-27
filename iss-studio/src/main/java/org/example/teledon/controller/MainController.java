package org.example.teledon.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.teledon.MainClass;
import org.example.teledon.domain.Timeslot;
import org.example.teledon.domain.User;
import org.example.teledon.service.MainService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class MainController {
    private MainService service;
    private User currentUser;
    private static final Logger logger = LogManager.getLogger();

    @FXML
    Button loginButton;

    @FXML
    DatePicker datePicker;

    ObservableList<Timeslot> timeslots = FXCollections.observableArrayList();
    @FXML
    ListView<Timeslot> timeslotListViewLeft;
    @FXML
    ListView<Timeslot> timeslotListViewRight;

    @FXML
    public void initialize() {
        for (ListView<Timeslot> timeslotListView : Arrays.asList(timeslotListViewLeft, timeslotListViewRight)) {
            timeslotListView.setCellFactory(cell -> new ListCell<>() {
                @Override
                protected void updateItem(Timeslot item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        String str = "";
                        str += item.getHour();
                        if (item.isBooked()) {
                            setStyle("-fx-background-color: #f1f1f1");
                        }
                        else {
                            setStyle("-fx-background-color: #ffffff");
                        }
                        setStyle(getStyle() + "; -fx-pref-height: 56px");
                        setText(str);
                    }
                }
            });
        }

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            logger.info("DATE CHANGED FROM {} TO {}", oldValue, newValue);
            setTables(newValue);
        });
    }

    public void initData(MainService service) {
        this.service = service;
//        this.service.addObserver(this);
        logger.info(LocalDate.now().toEpochDay());
        setTables(LocalDate.now());
    }

    private void setTables(LocalDate date) {
        timeslots.setAll(service.getTimeslots(date));
        timeslotListViewLeft.setItems(FXCollections.observableArrayList(timeslots.subList(0, 7)));
        timeslotListViewRight.setItems(FXCollections.observableArrayList(timeslots.subList(7, 14)));

    }

    public void spawnLoginWindow() {
        logger.info("spawning login window");

        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("views/LoginView.fxml"));
            root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Log in");
            stage.setScene(new Scene(root, 275, 180));
            stage.setResizable(false);
            stage.show();

            LoginController loginController = fxmlLoader.getController();
            loginController.initData(service, this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleBook() {
        logger.info("BOOKING");
    }

    public void handleShowBookings() {
        logger.info("SHOW BOOKINGS");
    }

    public void handleLogin() {
        if (currentUser == null) {
            logger.info("LOG IN");
            spawnLoginWindow();
        }
        else {
            currentUser = null;
            updateLoginButton();
        }
    }

    public void setUser(User user) {
        currentUser = user;
        updateLoginButton();
        logger.info("CURRENT USER SET: {}", user.getEmail());
    }

    private void updateLoginButton() {
        if (currentUser == null) {
            loginButton.setText("Log in");
        }
        else {
            loginButton.setText("Log out");
        }
    }
}
