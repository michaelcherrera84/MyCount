package com.michaelcherrera.mycount.model;

import jakarta.persistence.*;

/**
 * Represents a physical storage or inventory location where items are tracked.
 * Examples include dry storage, coolers, freezers, and makelines.
 */
@Entity
@Table(name = "locations", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "name", "store_id" })
})
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    /**
     * Default constructor required by JPA.
     */
    public Location() {}

    /**
     * Constructs a Location with the specified name and owning store.
     *
     * @param name the display name of the location (e.g., "Dry Storage", "Cooler 1")
     * @param store the store that owns this location
     */
    public Location(String name, Store store) {
        this.name = name;
        this.store = store;
    }

    /**
     * Returns the unique identifier for this location.
     *
     * @return the location ID, or {@code null} if not yet persisted
     */
    public Long getId() { return id; }

    /**
     * Returns the display name of this location.
     *
     * @return the location name
     */
    public String getName() { return name; }

    /**
     * Sets the display name of this location.
     *
     * @param name the new name for the location
     */
    public void setName(String name) { this.name = name; }

    /**
     * Returns the store that owns this location.
     *
     * @return the owning store
     */
    public Store getStore() { return store; }

    /**
     * Sets the store that owns this location.
     *
     * @param store the new owning store
     */
    public void setStore(Store store) { this.store = store; }
}
