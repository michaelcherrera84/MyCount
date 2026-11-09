package com.michaelcherrera.mycount.repository;

import com.michaelcherrera.mycount.model.Item;
import com.michaelcherrera.mycount.model.ItemLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for managing {@link ItemLocation} entities.
 * Provides standard CRUD operations as well as queries for finding
 * all locations associated with a specific item.
 */
@Repository
public interface ItemLocationRepository extends JpaRepository<ItemLocation, Long> {

    /**
     * Finds all locations where a specific item is tracked.
     *
     * @param item the inventory item
     * @return a list of {@link ItemLocation} entries for the given item
     */
    List<ItemLocation> findByItem(Item item);
}
