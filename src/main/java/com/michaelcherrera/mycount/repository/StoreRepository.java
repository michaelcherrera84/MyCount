package com.michaelcherrera.mycount.repository;

import com.michaelcherrera.mycount.model.Store;
import com.michaelcherrera.mycount.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for managing {@link Store} entities.
 * Provides standard CRUD operations as well as custom queries for
 * looking up stores by identifier, owner, and accessor permissions.
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    /**
     * Finds a store by its identifier code within a specific owner's account.
     * Store identifiers are unique per owner, so this returns at most one result.
     *
     * @param identifier the short, unique code for the store
     * @param owner the user who owns the store
     * @return an {@link Optional} containing the store if found, or empty otherwise
     */
    Optional<Store> findByIdentifierAndOwner(String identifier, User owner);

    /**
     * Finds all stores owned by a specific user.
     *
     * @param owner the user who owns the stores
     * @return a list of stores owned by the given user
     */
    List<Store> findByOwner(User owner);

    /**
     * Finds all stores where a specific user has accessor-level permissions.
     *
     * @param accessor the user whose accessible stores are being queried
     * @return a list of stores the user can access as an accessor
     */
    List<Store> findByAccessorsContaining(User accessor);
}
