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
import org.example.teledon.service.MainService;

import java.io.IOException;
import java.util.List;


public class BookingController {
    private MainService service;
    private MainController mainController;
    private static final Logger logger = LogManager.getLogger();

    ObservableList<Booking> bookings = FXCollections.observableArrayList();
    @FXML
    ListView<Booking> bookingListView;

    @FXML
    public void initialize() {
        bookingListView.setCellFactory(cell -> new ListCell<>() {
            @Override
            protected void updateItem(Booking item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    String str = "%s | %d-%d | %s".formatted(
                            item.getDay().toString(),
                            item.getStartHour(),
                            item.getEndHour(),
                            item.getStatus().toString()
                            );
                    setText(str);
                }
            }
        });
    }

    public void initData(MainService service, MainController mainController) {
        this.service = service;
        this.mainController = mainController;
        updateBookings(service.getBookings(mainController.getUser()));
    }

    public void updateBookings(List<Booking> bookingList) {
        logger.info("SET BOOKINGS");
        bookings.setAll(bookingList);
        bookingListView.setItems(bookings);
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
            modifyBookingController.initData(service, mainController, this, booking);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleModify() {
        if (bookingListView.getSelectionModel().isEmpty()) return;
        Booking booking = bookingListView.getSelectionModel().getSelectedItem();
        logger.info("MODIFY BOOKING");

        spawnModifyDialog(booking);
    }

    public void handleCancel() {
        if (bookingListView.getSelectionModel().isEmpty()) return;
        Booking booking = bookingListView.getSelectionModel().getSelectedItem();
        logger.info("CANCEL BOOKING");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Cancel booking on " + booking.getDay() + "?",
                ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            service.cancelBooking(booking);
            updateBookings(service.getBookings(mainController.getUser()));
            mainController.setTables(mainController.datePicker.getValue());

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Booking cancelled", ButtonType.OK);
            alert2.showAndWait();
        }
    }
}
