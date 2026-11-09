package com.michaelcherrera.mycount.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an inventory item that can be tracked across one or more locations.
 * Each item has a name, optional description, and a base unit of measurement
 * used to calculate quantities across its associated locations and units.
 */
@Entity
@Table(name = "items", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "name", "store_id" })
})
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_unit_id", nullable = false)
    private Unit baseUnit;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ItemLocation> locations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    /**
     * Default constructor required by JPA.
     */
    public Item() {}

    /**
     * Constructs an Item with the specified attributes.
     *
     * @param id the unique identifier for the item
     * @param name the display name of the item
     * @param description a human-readable description of the item, may be {@code null}
     * @param baseUnit the base unit of measurement for this item
     * @param store the store that owns this item
     */
    public Item(Long id, String name, String description, Unit baseUnit, Store store) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.baseUnit = baseUnit;
        this.store = store;
    }

    /**
     * Returns the unique identifier for this item.
     *
     * @return the item ID, or {@code null} if not yet persisted
     */
    public Long getId() { return id; }

    /**
     * Returns the display name of this item.
     *
     * @return the item name
     */
    public String getName() { return name; }

    /**
     * Sets the display name of this item.
     *
     * @param name the new name for the item
     */
    public void setName(String name) { this.name = name; }

    /**
     * Returns the human-readable description of this item.
     *
     * @return the description, or {@code null} if not set
     */
    public String getDescription() { return description; }

    /**
     * Sets the human-readable description of this item.
     *
     * @param description the new description, or {@code null} to clear it
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Returns the base unit of measurement for this item.
     *
     * @return the base unit
     */
    public Unit getBaseUnit() { return baseUnit; }

    /**
     * Sets the base unit of measurement for this item.
     *
     * @param baseUnit the new base unit
     */
    public void setBaseUnit(Unit baseUnit) { this.baseUnit = baseUnit; }

    /**
     * Returns the list of locations where this item is tracked.
     *
     * @return an unmodifiable list of {@link ItemLocation} associations
     */
    public List<ItemLocation> getLocations() { return locations; }

    /**
     * Returns the store that owns this item.
     *
     * @return the owning store
     */
    public Store getStore() { return store; }

    /**
     * Sets the store that owns this item.
     *
     * @param store the new owning store
     */
    public void setStore(Store store) { this.store = store; }
}
