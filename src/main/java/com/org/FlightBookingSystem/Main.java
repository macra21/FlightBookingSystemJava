package com.org.FlightBookingSystem;

import com.org.FlightBookingSystem.domain.Employee;
import com.org.FlightBookingSystem.repository.IEmployeeRepository;
import com.org.FlightBookingSystem.repository.IFlightRepository;
import com.org.FlightBookingSystem.repository.jdbc.EmployeeJdbcRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        logger.trace("Starting application.");
        IEmployeeRepository employeeRepository = new EmployeeJdbcRepository();

        Employee employee = new Employee("Giani", "ParolaSecreta");
        employeeRepository.save(employee);

        Employee employee2 = employeeRepository.findOne(employee.getId());


    }
}
