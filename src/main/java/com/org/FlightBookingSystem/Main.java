package com.org.FlightBookingSystem;

import com.org.FlightBookingSystem.controller.LoginController;
import com.org.FlightBookingSystem.repository.jdbc.BookingJdbcRepository;
import com.org.FlightBookingSystem.repository.jdbc.EmployeeJdbcRepository;
import com.org.FlightBookingSystem.repository.jdbc.FlightJdbcRepository;
import com.org.FlightBookingSystem.service.BookingService;
import com.org.FlightBookingSystem.service.EmployeeService;
import com.org.FlightBookingSystem.service.FlightService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        EmployeeService employeeService = new EmployeeService(new EmployeeJdbcRepository());
        FlightService flightService = new FlightService(new FlightJdbcRepository());
        BookingService bookingService = new BookingService(new BookingJdbcRepository());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login-view.fxml"));
        Scene scene = new Scene(loader.load(), 300, 250);

        LoginController loginController = loader.getController();
        loginController.setup(employeeService, flightService, bookingService);

        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
