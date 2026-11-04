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
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_unit_id", nullable = false)
    private Unit baseUnit;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemLocation> locations = new ArrayList<>();

    public Item() {}

    public Item(Long id, String name, String description, Unit baseUnit) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.baseUnit = baseUnit;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Unit getBaseUnit() { return baseUnit; }

    public void setBaseUnit(Unit baseUnit) { this.baseUnit = baseUnit; }

    public List<ItemLocation> getLocations() { return locations; }
}
