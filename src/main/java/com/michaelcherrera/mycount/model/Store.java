package com.michaelcherrera.mycount.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * This class encapsulates the details of a store, including its unique identifier,
 * display name, owner, accessors, and active status. It provides methods to manage
 * these attributes and is mapped to the "stores" table in the database.
 */
@Entity
@Table(name = "stores", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "identifier", "owner_id" })
})
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String identifier;

    @Column(nullable = false)
    private String displayName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "store_accessors",
            joinColumns = @JoinColumn(name = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private final Set<User> accessors = new HashSet<>();

    @Column(nullable = false)
    private boolean active = true;

    /**
     * Default constructor required by JPA.
     */
    public Store() {}

    /**
     * Constructs a Store with the specified identifier, display name, and owner.
     *
     * @param identifier a short, unique code for the store (e.g., "STORE-001")
     * @param displayName the human-readable name shown in the UI
     * @param owner the user who owns this store
     */
    public Store(String identifier, String displayName, User owner) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.owner = owner;
    }

    /**
     * Returns the unique UUID identifier for this store.
     *
     * @return the store ID
     */
    public UUID getId() { return id; }

    /**
     * Returns the short, unique identifier code for this store.
     *
     * @return the store identifier
     */
    public String getIdentifier() { return identifier; }

    /**
     * Sets the short, unique identifier code for this store.
     *
     * @param identifier the new identifier
     */
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    /**
     * Returns the human-readable display name for this store.
     *
     * @return the display name
     */
    public String getDisplayName() { return displayName; }

    /**
     * Sets the human-readable display name for this store.
     *
     * @param displayName the new display name
     */
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    /**
     * Returns the user who owns this store.
     *
     * @return the owner user
     */
    public User getOwner() { return owner; }

    /**
     * Sets the user who owns this store.
     *
     * @param owner the new owner user
     */
    public void setOwner(User owner) { this.owner = owner; }

    /**
     * Returns the set of users who have accessor-level permissions on this store.
     * Accessors can view and manage inventory but do not own the store.
     *
     * @return an unmodifiable set of accessor users
     */
    public Set<User> getAccessors() { return accessors; }

    /**
     * Adds a user as an accessor to this store, granting them permission to
     * view and manage inventory. Also updates the user's accessible stores list.
     *
     * @param accessor the user to add as an accessor
     */
    public void addAccessor(User accessor) {
        this.accessors.add(accessor);
        accessor.addAccessibleStore(this);
    }

    /**
     * Removes a user as an accessor from this store, revoking their permission.
     * Also updates the user's accessible stores list.
     *
     * @param accessor the user to remove as an accessor
     */
    public void removeAccessor(User accessor) {
        this.accessors.remove(accessor);
        accessor.removeAccessibleStore(this);
    }

    /**
     * Returns whether this store is currently active.
     * Inactive stores are retained in the database but hidden from active operations.
     *
     * @return {@code true} if the store is active
     */
    public boolean getActive() { return active; }

    /**
     * Sets whether this store is currently active.
     *
     * @param active {@code true} to activate the store, {@code false} to deactivate it
     */
    public void setActive(boolean active) { this.active = active; }
}