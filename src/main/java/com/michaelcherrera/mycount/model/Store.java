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

    public Store() {}

    public Store(String identifier, String displayName, User owner) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.owner = owner;
    }

    public UUID getId() { return id; }

    public String getIdentifier() { return identifier; }

    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getDisplayName() { return displayName; }

    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public User getOwner() { return owner; }

    public void setOwner(User owner) { this.owner = owner; }

    public Set<User> getAccessors() { return accessors; }

    public void addAccessor(User accessor) {
        this.accessors.add(accessor);
        accessor.addAccessibleStore(this);
    }

    public void removeAccessor(User accessor) {
        this.accessors.remove(accessor);
        accessor.removeAccessibleStore(this);
    }

    public boolean getActive() { return active; }

    public void setActive(boolean active) { this.active = active; }
}