package com.michaelcherrera.mycount.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the association between an inventory item and a physical location.
 * Defines whether the location is the item's base location and maintains the
 * collection of units and their conversion factors available at that location.
 */
@Entity
@Table(name = "item_locations", uniqueConstraints = { @UniqueConstraint(columnNames = { "item_id", "location_id" }) })
public class ItemLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    private boolean baseLocation;

    @OneToMany(mappedBy = "itemLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ItemLocationUnit> units = new ArrayList<>();

    /**
     * Default constructor required by JPA.
     */
    public ItemLocation() {}

    /**
     * Constructs an ItemLocation linking an item to a physical location.
     *
     * @param item the inventory item
     * @param location the physical location where the item is stored
     */
    public ItemLocation(Item item, Location location) {
        this.item = item;
        this.location = location;
    }

    /**
     * Returns the unique identifier for this item-location association.
     *
     * @return the ID, or {@code null} if not yet persisted
     */
    public Long getId() { return id; }

    /**
     * Returns the inventory item associated with this location.
     *
     * @return the item
     */
    public Item getItem() { return item; }

    /**
     * Sets the inventory item for this location association.
     *
     * @param item the item to associate
     */
    public void setItem(Item item) { this.item = item; }

    /**
     * Returns the physical location for this item.
     *
     * @return the location
     */
    public Location getLocation() { return location; }

    /**
     * Sets the physical location for this item.
     *
     * @param location the location to associate
     */
    public void setLocation(Location location) { this.location = location; }

    /**
     * Returns whether this location is the base location for the item.
     * The base location is the primary storage area used for default quantity calculations.
     *
     * @return {@code true} if this is the base location
     */
    public boolean isBaseLocation() { return baseLocation; }

    /**
     * Sets whether this location is the base location for the item.
     *
     * @param baseLocation {@code true} to mark as the base location
     */
    public void setBaseLocation(boolean baseLocation) { this.baseLocation = baseLocation; }

    /**
     * Returns the list of units available for this item at this location.
     *
     * @return a list of {@link ItemLocationUnit} entries with conversion factors
     */
    public List<ItemLocationUnit> getUnits() { return units; }
}
