package flightbookingsystem;

import flightbookingsystem.model.Flight;
import flightbookingsystem.repository.db.FlightDBRepository;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        FlightDBRepository flightRepo = new FlightDBRepository();

        System.out.println("Executing operation: SAVE");
        LocalDateTime departureTime = LocalDateTime.now().plusDays(2);
        Flight newFlight = new Flight(
                null,
                "Cluj-Napoca",
                "London",
                departureTime,
                departureTime.plusHours(3),
                180
        );
        flightRepo.save(newFlight);
        System.out.println(String.format("Insert:  ID: %d | From: %s | To: %s | Seats: %d",
                newFlight.getId(),
                newFlight.getDepartureAirport(),
                newFlight.getArrivalAirport(),
                newFlight.getAvailableSeats()
        ));
        System.out.println("Flight saved to database.");

        Iterable<Flight> allFlights = flightRepo.findAll();
        if (allFlights.iterator().hasNext()) {
            Flight flightToUpdate = allFlights.iterator().next();
            flightToUpdate.setAvailableSeats(27);
            flightRepo.update(flightToUpdate);
            System.out.println(String.format("Update:  ID: %d | From: %s | To: %s | Seats: %d",
                    flightToUpdate.getId(),
                    flightToUpdate.getDepartureAirport(),
                    flightToUpdate.getArrivalAirport(),
                    flightToUpdate.getAvailableSeats()
            ));
        }

        Iterable<Flight> results = flightRepo.findByDestinationAndDate("London", departureTime);

        int foundCount = 0;
        for (Flight f : results) {
            System.out.println(String.format("Match Found -> ID: %d | From: %s | To: %s | Seats: %d",
                    f.getId(),
                    f.getDepartureAirport(),
                    f.getArrivalAirport(),
                    f.getAvailableSeats()
            ));
            foundCount++;
        }

        if (foundCount == 0) {
            System.out.println("No records found matching the criteria.");
        }
    }
}