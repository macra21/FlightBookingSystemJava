package com.org.FlightBookingSystem.domain;

/**
 * Generic interface for domain entities.
 * @param <ID> the type of the unique identifier
 */
public interface IEntity<ID> {
    ID getId();
    void setId(ID id);
}
