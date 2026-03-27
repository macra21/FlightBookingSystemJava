package com.org.FlightBookingSystem.validators;

import com.org.FlightBookingSystem.domain.Employee;
import com.org.FlightBookingSystem.exceptions.ValidationException;

public class EmployeeValidator{
    public static void validate(Employee employee){
        StringBuilder errors = new StringBuilder();
        if (employee.getId() != null && employee.getId() < 0){
            errors.append("Employee ID should be greater than or equal to 0.\n");
        }
        if (employee.getUsername().isEmpty() || employee.getUsername() == null){
            errors.append("Employee username cannot be empty.\n");
        }
        if (employee.getPassword().isEmpty() || employee.getPassword() == null){
            errors.append("Employee password cannot be empty.\n");
        } else if (employee.getPassword().length() < 8){
            errors.append("Employee password should be at least 8 characters.\n");
        }
        if (!errors.isEmpty()){
            throw new ValidationException(errors.toString());
        }
    }
}
