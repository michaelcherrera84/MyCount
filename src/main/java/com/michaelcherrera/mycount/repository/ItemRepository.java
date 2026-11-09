package com.michaelcherrera.mycount.repository;

import com.michaelcherrera.mycount.model.Item;
import com.michaelcherrera.mycount.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link Item} entities.
 * Provides standard CRUD operations as well as custom queries for
 * looking up items by name within a specific store.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Finds an item by its name within a specific store.
     * Item names are unique per store, so this returns at most one result.
     *
     * @param name the name of the item
     * @param store the store that owns the item
     * @return an {@link Optional} containing the item if found, or empty otherwise
     */
    Optional<Item> findByNameAndStore(String name, Store store);
}
