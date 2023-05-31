package org.example.teledon.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.teledon.MainClass;
import org.example.teledon.domain.Booking;
import org.example.teledon.domain.Role;
import org.example.teledon.domain.Timeslot;
import org.example.teledon.domain.User;
import org.example.teledon.service.MainService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class MainController {
    private MainService service;
    private User currentUser;

    private Timeslot startHour;
    private Timeslot endHour;
    private Timeslot selectedBookingStartHour;

    private final int MAX_BOOKING_LENGTH = 6;
    private static final Logger logger = LogManager.getLogger();

    @FXML
    Button loginButton;

    @FXML
    Button clearSelectionButton;

    @FXML
    Button showBookingsButton;

    @FXML
    Button cancelBookingButton;
    @FXML
    Button modifyBookingButton;

    @FXML
    DatePicker datePicker;

    @FXML
    TextArea commentTextArea;

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
                        else if (startHour != null &&
                                endHour != null &&
                                item.getHour() >= startHour.getHour() &&
                                item.getHour() <= endHour.getHour()) {
                            logger.info("DISPLAY CURRENT SELECTION");
                            setStyle("-fx-background-color: #d9d9d9");
                        }
                        else if (startHour != null && item.getHour() == startHour.getHour()) {
                            logger.info("DISPLAY CURRENT SELECTION");
                            setStyle("-fx-background-color: #d9d9d9");
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
            startHour = null;
            endHour = null;
            clearSelectionButton.setVisible(false);
        });

        timeslotListViewLeft.setOnMouseClicked(event -> handleSelectHour(timeslotListViewLeft.getSelectionModel().getSelectedItem()));
        timeslotListViewRight.setOnMouseClicked(event -> handleSelectHour(timeslotListViewRight.getSelectionModel().getSelectedItem()));
    }

    public void initData(MainService service) {
        this.service = service;
//        this.service.addObserver(this);
        logger.info(LocalDate.now().toEpochDay());
        datePicker.setValue(LocalDate.now());
        setTables(LocalDate.now());
    }

    public void setTables(LocalDate date) {
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

    public void spawnBookingWindow() {
        logger.info("spawning booking window");

        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("views/BookingView.fxml"));
            root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Bookings");
            stage.setScene(new Scene(root, 400, 300));
            stage.setResizable(false);
            stage.show();

            BookingController bookingController = fxmlLoader.getController();
            bookingController.initData(service, this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void spawnModifyDialog(Booking booking) {
        logger.info("spawning modify dialog");

        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("views/ModifyBookingView.fxml"));
            root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Modify booking");
            stage.setScene(new Scene(root, 360, 150));
            stage.setResizable(false);
            stage.show();

            ModifyBookingController modifyBookingController = fxmlLoader.getController();
            modifyBookingController.initData(service, this, null, booking);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleBook() {
        if (currentUser == null ||
                datePicker.valueProperty().isNull().getValue() ||
                startHour == null ||
                endHour == null) {
            logger.warn("BOOKING FAILED");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all booking details", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        logger.info("BOOKING BY {} ON {} FROM {} TO {}. COMMENT: {}",
                currentUser.getEmail(),
                datePicker.valueProperty().getValue().toString(),
                startHour.getHour(),
                endHour.getHour(),
                commentTextArea.getText());

        service.addBooking(
                currentUser.getId(),
                datePicker.valueProperty().getValue(),
                startHour.getHour(),
                endHour.getHour(),
                commentTextArea.getText());

        commentTextArea.setText("");
        startHour = null;
        endHour = null;
        clearSelectionButton.setVisible(false);
        setTables(datePicker.getValue());

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Booking added", ButtonType.OK);
        alert.showAndWait();
    }

    public void handleShowBookings() {
        logger.info("SHOW BOOKINGS");
        spawnBookingWindow();
    }

    public void handleLogin() {
        if (currentUser == null) {
            logger.info("LOG IN");
            spawnLoginWindow();
        }
        else {
            currentUser = null;
            showBookingsButton.setVisible(false);
            cancelBookingButton.setVisible(false);
            modifyBookingButton.setVisible(false);
            updateLoginButton();
        }
    }

    public User getUser() { return currentUser; }

    public void setUser(User user) {
        currentUser = user;
        showBookingsButton.setVisible(true);

        if (user.getRole() == Role.EMPLOYEE) {
            cancelBookingButton.setVisible(true);
            modifyBookingButton.setVisible(true);
        }

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

    private void handleSelectHour(Timeslot timeslot) {
        logger.info("SELECTED {}", timeslot.getHour());
        if (startHour == null) {
            if (timeslots.get(timeslot.getHour() - 8).isBooked())
            {
                selectedBookingStartHour = timeslot;
                return;
            }

            startHour = timeslot;
            logger.info("SET START HOUR {}", timeslot.getHour());
        } else if (endHour == null) { // start already selected
            if (timeslot.getHour() - startHour.getHour() >= MAX_BOOKING_LENGTH) {
                return;
            }
            if (timeslot.getHour() < startHour.getHour()) {
                return;
            }
            for (int h = startHour.getHour(); h <= timeslot.getHour(); h++) {
                if (timeslots.get(h - 8).isBooked()) return;
            }
            endHour = timeslot;
            logger.info("SET END HOUR {}", timeslot.getHour());
        }
        updateTimeslots();
        clearSelectionButton.setVisible(true);
    }

    private void updateTimeslots() {
        timeslotListViewLeft.refresh();
        timeslotListViewRight.refresh();
    }

    public void handleClearSelection() {
        startHour = null;
        endHour = null;
        clearSelectionButton.setVisible(false);

        updateTimeslots();
    }

    public void handleCancel() {
        if (selectedBookingStartHour == null) return;
        Booking booking = service.getBooking(datePicker.getValue(), selectedBookingStartHour.getHour());
        logger.info("CANCEL BOOKING");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Cancel booking on " + booking.getDay() + "?",
                ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            service.cancelBooking(booking);
            setTables(datePicker.getValue());

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Booking cancelled", ButtonType.OK);
            alert2.showAndWait();
        }
    }

    public void handleModify() {
        if (selectedBookingStartHour == null) return;
        Booking booking = service.getBooking(datePicker.getValue(), selectedBookingStartHour.getHour());
        logger.info("MODIFY BOOKING");

        spawnModifyDialog(booking);
    }
}
