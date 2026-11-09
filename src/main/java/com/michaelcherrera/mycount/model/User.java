package com.michaelcherrera.mycount.model;

import jakarta.persistence.*;

import java.util.*;

/**
 * This class encapsulates the details of a user, including their unique identifier,
 * username, email, password, and relationships with stores and other users. It provides
 * methods to manage these attributes and is mapped to the "users" table in the database.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private final Set<Store> stores = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_supervision",
            joinColumns = @JoinColumn(name = "supervisor_id"),
            inverseJoinColumns = @JoinColumn(name = "subordinate_id")
    )
    private final Set<User> subordinates = new HashSet<>();

    @ManyToMany(mappedBy = "subordinates")
    private final Set<User> supervisors = new HashSet<>();

    @ManyToMany(mappedBy = "accessors")
    private final Set<Store> accessibleStores = new HashSet<>();

    /**
     * Default constructor required by JPA.
     */
    public User() {}

    /**
     * Constructs a User with the specified credentials.
     *
     * @param username the unique login name for the user
     * @param email the user's email address, may be {@code null}
     * @param password the user's password
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Returns the unique UUID identifier for this user.
     *
     * @return the user ID
     */
    public UUID getId() { return id; }

    /**
     * Returns the unique login name for this user.
     *
     * @return the username
     */
    public String getUsername() { return username; }

    /**
     * Sets the unique login name for this user.
     *
     * @param username the new username
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Returns the email address for this user.
     *
     * @return the email, or {@code null} if not set
     */
    public String getEmail() { return email; }

    /**
     * Sets the email address for this user.
     *
     * @param email the new email, or {@code null} to clear it
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Returns the password for this user.
     *
     * @return the password
     */
    public String getPassword() { return password; }

    /**
     * Sets the password for this user.
     *
     * @param password the new password
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Returns the set of stores owned by this user.
     *
     * @return an unmodifiable set of owned stores
     */
    public Set<Store> getStores() { return stores; }

    /**
     * Adds a store to this user's owned stores, setting this user as the owner
     * and also adding this user as an accessor on the store.
     *
     * @param store the store to add
     */
    public void addStore(Store store) {
        this.stores.add(store);
        store.setOwner(this);
        store.addAccessor(this);
    }

    /**
     * Returns the set of users who report directly to this user.
     *
     * @return an unmodifiable set of subordinate users
     */
    public Set<User> getSubordinates() { return subordinates; }

    /**
     * Adds a user as a subordinate of this user, establishing a supervisor-subordinate
     * relationship in both directions.
     *
     * @param subordinate the user to add as a subordinate
     */
    public void addSubordinate(User subordinate) {
        this.subordinates.add(subordinate);
        subordinate.getSupervisors().add(this);
    }

    /**
     * Removes a user as a subordinate of this user, removing the supervisor-subordinate
     * relationship in both directions.
     *
     * @param subordinate the user to remove as a subordinate
     */
    public void removeSubordinate(User subordinate) {
        this.subordinates.remove(subordinate);
        subordinate.getSupervisors().remove(this);
    }

    /**
     * Returns the set of users who supervise this user.
     *
     * @return an unmodifiable set of supervisor users
     */
    public Set<User> getSupervisors() { return supervisors; }

    /**
     * Adds a user as a supervisor of this user, establishing the relationship
     * in both directions.
     *
     * @param supervisor the user to add as a supervisor
     */
    public void addSupervisor(User supervisor) {
        this.supervisors.add(supervisor);
        supervisor.getSubordinates().add(this);
    }

    /**
     * Removes a user as a supervisor of this user, removing the relationship
     * in both directions.
     *
     * @param supervisor the user to remove as a supervisor
     */
    public void removeSupervisor(User supervisor) {
        this.supervisors.remove(supervisor);
        supervisor.getSubordinates().remove(this);
    }

    /**
     * Returns the set of stores this user can access as an accessor (non-owner).
     *
     * @return an unmodifiable set of accessible stores
     */
    public Set<Store> getAccessibleStores() { return accessibleStores; }

    /**
     * Adds a store to this user's accessible stores, also adding this user
     * as an accessor on the store.
     *
     * @param store the store to add
     */
    public void addAccessibleStore(Store store) {
        this.accessibleStores.add(store);
        store.addAccessor(this);
    }

    /**
     * Removes a store from this user's accessible stores, also removing this user
     * as an accessor on the store.
     *
     * @param store the store to remove
     */
    public void removeAccessibleStore(Store store) {
        this.accessibleStores.remove(store);
        store.getAccessors().remove(this);
    }
}
