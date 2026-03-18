package com.org.FlightBookingSystem.repository;


import com.org.FlightBookingSystem.domain.IEntity;

/**
 * Defines basic CRUD operations for a repository.
 *
 * @param <ID> the type of the entity's identifier
 * @param <E>  the type of the entity, must implement IEntity
 */
public interface IRepository<ID, E extends IEntity<ID>> {
    /**
     * Saves a new entity to the data store.
     * @param entity the entity to persist
     */
    void save(E entity);

    /**
     * Retrieves an entity by its unique identifier.
     * @param id the ID of the entity
     * @return the found entity or null if not found
     */
    E findOne(ID id);

    /**
     * Retrieves all entities of this type.
     * @return an iterable collection of all entities
     */
    Iterable<E> findAll();

    /**
     * Updates an existing entity in the data store.
     * @param entity the entity with updated information
     */
    void update(E entity);

    /**
     * Deletes an entity based on its unique identifier.
     * @param id the ID of the entity to remove
     */
    void delete(ID id);
}