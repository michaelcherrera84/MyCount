package com.michaelcherrera.mycount.repository;

import com.michaelcherrera.mycount.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data JPA repository for managing {@link User} entities.
 * Provides standard CRUD operations as well as custom queries for
 * looking up users by username or email address.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Finds a user by their unique username.
     *
     * @param username the login name of the user
     * @return an {@link Optional} containing the user if found, or empty otherwise
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user
     * @return an {@link Optional} containing the user if found, or empty otherwise
     */
    Optional<User> findByEmail(String email);
}
