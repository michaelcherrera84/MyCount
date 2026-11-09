package com.michaelcherrera.mycount.repository;

import com.michaelcherrera.mycount.model.Store;
import com.michaelcherrera.mycount.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link Unit} entities.
 * Provides standard CRUD operations as well as custom queries for
 * looking up units of measurement by name within a specific store.
 */
@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    /**
     * Finds a unit of measurement by its name within a specific store.
     * Unit names are unique per store, so this returns at most one result.
     *
     * @param name the name of the unit
     * @param store the store that owns the unit
     * @return an {@link Optional} containing the unit if found, or empty otherwise
     */
    Optional<Unit> findByNameAndStore(String name, Store store);
}
