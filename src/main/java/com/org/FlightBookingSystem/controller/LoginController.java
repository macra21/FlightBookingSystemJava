package com.org.FlightBookingSystem.controller;

import com.org.FlightBookingSystem.domain.Employee;
import com.org.FlightBookingSystem.service.BookingService;
import com.org.FlightBookingSystem.service.EmployeeService;
import com.org.FlightBookingSystem.service.FlightService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private EmployeeService employeeService;
    private FlightService flightService;
    private BookingService bookingService;

    public void setup(EmployeeService employeeService, FlightService flightService, BookingService bookingService) {
        this.employeeService = employeeService;
        this.flightService = flightService;
        this.bookingService = bookingService;
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            Employee loggedEmployee = employeeService.login(username, password);
            openMainWindow(loggedEmployee);
        } catch (Exception e) {
            errorLabel.setText("Login failed: " + e.getMessage());
        }
    }

    private void openMainWindow(Employee employee) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main-view.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load(), 800, 600));

        MainController mainController = loader.getController();
        mainController.setup(employee, flightService, bookingService);

        stage.setTitle("Flight Booking System - Dashboard");
        stage.show();

        ((Stage) usernameField.getScene().getWindow()).close();
    }
}
