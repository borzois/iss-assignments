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
import org.example.teledon.domain.Timeslot;
import org.example.teledon.service.MainService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


public class ModifyBookingController {
    private MainService service;
    private MainController mainController;
    private Booking booking;
    private static final Logger logger = LogManager.getLogger();

    @FXML
    private DatePicker datePicker;

    ObservableList<Timeslot> timeslots = FXCollections.observableArrayList();

    @FXML
    private ComboBox<Timeslot> startHourComboBox;
    @FXML
    private ComboBox<Timeslot> endHourComboBox;
    private BookingController bookingController;

    private static class TimeslotListCell extends ListCell<Timeslot> {
        @Override
        protected void updateItem(Timeslot item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                String str = "";
                str += item.getHour();
                setText(str);
            }
        }
    }

    @FXML
    public void initialize() {
        startHourComboBox.setCellFactory(cell -> new TimeslotListCell());
        endHourComboBox.setCellFactory(cell -> new TimeslotListCell());
        startHourComboBox.setButtonCell(new TimeslotListCell());
        endHourComboBox.setButtonCell(new TimeslotListCell());
    }

    public void initData(MainService service, MainController mainController, BookingController bookingController, Booking booking) {
        this.service = service;
        this.mainController = mainController;
        this.bookingController = bookingController;
        this.booking = booking;

        timeslots.setAll(service.getTimeslots(booking.getDay()));
        startHourComboBox.setItems(timeslots);
        endHourComboBox.setItems(timeslots);

        startHourComboBox.getSelectionModel().select(booking.getStartHour() - 8);
        endHourComboBox.getSelectionModel().select(booking.getEndHour() - 8);

        datePicker.setValue(booking.getDay());
    }

    public void handleModify() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Modify booking?",
                ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            service.modifyBooking(new Booking(
                    booking.getId(),
                    booking.getStatus(),
                    booking.getComment(),
                    booking.getUserId(),
                    datePicker.getValue(),
                    startHourComboBox.getValue().getHour(),
                    endHourComboBox.getValue().getHour()
            ));

            if (bookingController != null)
                bookingController.updateBookings(service.getBookings(mainController.getUser()));
            mainController.setTables(mainController.datePicker.getValue());

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Booking modified", ButtonType.OK);
            alert2.showAndWait();
        }
    }
}
