package com.michaelcherrera.mycount.repository;

import com.michaelcherrera.mycount.model.ItemLocation;
import com.michaelcherrera.mycount.model.ItemLocationUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for managing {@link ItemLocationUnit} entities.
 * Provides standard CRUD operations as well as queries for finding
 * all units configured for a specific item-location combination.
 */
@Repository
public interface ItemLocationUnitRepository extends JpaRepository<ItemLocationUnit, Long> {

    /**
     * Finds all units available for a specific item at a specific location.
     *
     * @param itemLocation the item-location association
     * @return a list of {@link ItemLocationUnit} entries with their conversion factors
     */
    List<ItemLocationUnit> findByItemLocation(ItemLocation itemLocation);
}
