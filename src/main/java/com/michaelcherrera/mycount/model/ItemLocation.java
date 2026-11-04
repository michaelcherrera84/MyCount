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
    private List<ItemLocationUnit> units = new ArrayList<>();

    public ItemLocation() {}

    public ItemLocation(Item item, Location location) {
        this.item = item;
        this.location = location;
    }

    public Long getId() { return id; }

    public Item getItem() { return item; }

    public void setItem(Item item) { this.item = item; }

    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }

    public boolean isBaseLocation() { return baseLocation; }

    public void setBaseLocation(boolean baseLocation) { this.baseLocation = baseLocation; }

    public List<ItemLocationUnit> getUnits() { return units; }
}
