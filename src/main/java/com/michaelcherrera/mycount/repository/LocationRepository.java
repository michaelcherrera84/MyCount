package com.michaelcherrera.mycount.repository;

import com.michaelcherrera.mycount.model.Location;
import com.michaelcherrera.mycount.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link Location} entities.
 * Provides standard CRUD operations as well as custom queries for
 * looking up locations by name within a specific store.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    /**
     * Finds a location by its name within a specific store.
     * Location names are unique per store, so this returns at most one result.
     *
     * @param name the name of the location
     * @param store the store that owns the location
     * @return an {@link Optional} containing the location if found, or empty otherwise
     */
    Optional<Location> findByNameAndStore(String name, Store store);
}
