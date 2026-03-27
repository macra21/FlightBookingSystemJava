package com.org.FlightBookingSystem.controller;

import com.org.FlightBookingSystem.domain.Booking;
import com.org.FlightBookingSystem.domain.Employee;
import com.org.FlightBookingSystem.domain.Flight;
import com.org.FlightBookingSystem.service.BookingService;
import com.org.FlightBookingSystem.service.FlightService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainController {

    @FXML private Label welcomeLabel;

    @FXML private TableView<Flight> flightsTable;
    @FXML private TableView<Flight> searchTable;
    @FXML private TableView<Booking> bookingsTable;

    @FXML private TextField searchDestinationField;
    @FXML private DatePicker searchDatePicker;

    private Employee loggedEmployee;
    private FlightService flightService;
    private BookingService bookingService;

    private ObservableList<Flight> flightsModel = FXCollections.observableArrayList();
    private ObservableList<Flight> searchModel = FXCollections.observableArrayList();
    private ObservableList<Booking> bookingsModel = FXCollections.observableArrayList();

    public void setup(Employee employee, FlightService flightService, BookingService bookingService) {
        this.loggedEmployee = employee;
        this.flightService = flightService;
        this.bookingService = bookingService;

        welcomeLabel.setText("Agent: " + employee.getUsername());

        flightsTable.setItems(flightsModel);
        searchTable.setItems(searchModel);
        bookingsTable.setItems(bookingsModel);

        loadData();
    }

    private void loadData() {
        try {
            flightsModel.clear();
            Iterable<Flight> allFlights = flightService.findAll();
            for (Flight f : allFlights) {
                if (f.getAvailableSeats() > 0) {
                    flightsModel.add(f);
                }
            }

            bookingsModel.clear();
            Iterable<Booking> allBookings = bookingService.findAll();
            for (Booking b : allBookings) {
                bookingsModel.add(b);
            }

            handleSearch();
        } catch (Exception e) {
            showAlert("Load Error", "Failed to load data: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleSearch() {
        searchModel.clear();
        String destination = searchDestinationField.getText();
        LocalDate date = searchDatePicker.getValue();

        if (destination == null || destination.trim().isEmpty() || date == null) {
            return;
        }

        for (Flight f : flightsModel) {
            if (f.getArrivalAirport().trim().equalsIgnoreCase(destination.trim()) &&
                    f.getDepartureTime().toLocalDate().equals(date)) {
                searchModel.add(f);
            }
        }
    }

    @FXML
    public void handleClearSearch() {
        searchDestinationField.clear();
        searchDatePicker.setValue(null);
        searchModel.clear();
    }

    @FXML
    public void handleBookFlight() {
        Flight tempFlight = searchTable.getSelectionModel().getSelectedItem();
        Flight selectedFlight = (tempFlight != null) ? tempFlight : flightsTable.getSelectionModel().getSelectedItem();

        if (selectedFlight == null) {
            showAlert("Selection Error", "Please select a flight to book!", Alert.AlertType.WARNING);
            return;
        }

        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Book Flight");
        dialog.setHeaderText("Dest: " + selectedFlight.getArrivalAirport() + " | Available seats: " + selectedFlight.getAvailableSeats());

        ButtonType bookButtonType = new ButtonType("Confirm Booking", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(bookButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextArea touristNamesArea = new TextArea();
        touristNamesArea.setPromptText("Ex: Ion Popescu, Maria Popescu");
        grid.add(new Label("Tourist Names (comma separated):"), 0, 0);
        grid.add(touristNamesArea, 1, 0);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(touristNamesArea::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == bookButtonType) {
                String input = touristNamesArea.getText();
                if (input != null && !input.trim().isEmpty()) {
                    return Arrays.stream(input.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toList());
                }
            }
            return null;
        });

        Optional<List<String>> result = dialog.showAndWait();
        result.ifPresent(names -> {
            int requestedSeats = names.size();

            if (requestedSeats == 0) return;
            if (requestedSeats > selectedFlight.getAvailableSeats()) {
                showAlert("Error", "Not enough available seats!", Alert.AlertType.ERROR);
                return;
            }

            try {
                Booking booking = new Booking(selectedFlight, requestedSeats, names);
                bookingService.save(booking);

                selectedFlight.setAvailableSeats(selectedFlight.getAvailableSeats() - requestedSeats);
                flightService.update(selectedFlight);

                showAlert("Success", "Booking created successfully.", Alert.AlertType.INFORMATION);

                loadData();
            } catch (Exception e) {
                showAlert("Error", "Booking failed: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    public void handleModifyBooking() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("No Selection", "Please select a booking from the Manage Bookings table.", Alert.AlertType.WARNING);
            return;
        }

        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Modify Booking");
        dialog.setHeaderText("Changing tourists for Booking. Originally " + selectedBooking.getNumberOfSeats() + " seats.");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        TextArea touristNamesArea = new TextArea();
        String currentNames = String.join(", ", selectedBooking.getTouristNames());
        touristNamesArea.setText(currentNames);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("New Names (comma separated):"), 0, 0);
        grid.add(touristNamesArea, 1, 0);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(touristNamesArea::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                String input = touristNamesArea.getText();
                if (input != null && !input.trim().isEmpty()) {
                    return Arrays.stream(input.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toList());
                }
            }
            return null;
        });

        Optional<List<String>> result = dialog.showAndWait();
        result.ifPresent(newNames -> {
            if (newNames.size() != selectedBooking.getNumberOfSeats()) {
                showAlert("Validation Error", "You must provide EXACTLY " + selectedBooking.getNumberOfSeats() + " names to match the bought seats.", Alert.AlertType.ERROR);
                return;
            }

            try {
                selectedBooking.setTouristNames(newNames);
                bookingService.update(selectedBooking);

                showAlert("Success", "Ticket names updated successfully.", Alert.AlertType.INFORMATION);
                loadData();
            } catch (Exception e) {
                showAlert("Update Error", "Failed to update booking names: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    public void handleLogout() {
        welcomeLabel.getScene().getWindow().hide();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
