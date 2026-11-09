package com.michaelcherrera.mycount.model;

import jakarta.persistence.*;

/**
 * Represents a unit of measurement used to track inventory quantities.
 * Examples include cases, bags, boxes, and storage containers.
 */
@Entity
@Table(name = "units", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "store_id"})
})
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String abbreviation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    /**
     * Default constructor required by JPA.
     */
    public Unit() {}

    /**
     * Constructs a Unit with the specified attributes.
     *
     * @param name the full name of the unit (e.g., "Case", "Bag")
     * @param description a human-readable description, may be {@code null}
     * @param abbreviation a short abbreviation for display (e.g., "CS", "BG"), may be {@code null}
     * @param store the store that owns this unit
     */
    public Unit(String name, String description, String abbreviation,  Store store) {
        this.name = name;
        this.description = description;
        this.abbreviation = abbreviation;
        this.store = store;
    }

    /**
     * Returns the unique identifier for this unit.
     *
     * @return the unit ID, or {@code null} if not yet persisted
     */
    public Long getId() { return id; }

    /**
     * Returns the full name of this unit.
     *
     * @return the unit name
     */
    public String getName() { return name; }

    /**
     * Sets the full name of this unit.
     *
     * @param name the new name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Returns the human-readable description of this unit.
     *
     * @return the description, or {@code null} if not set
     */
    public String getDescription() { return description; }

    /**
     * Sets the human-readable description of this unit.
     *
     * @param description the new description, or {@code null} to clear it
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Returns the short abbreviation for this unit, used for compact display.
     *
     * @return the abbreviation, or {@code null} if not set
     */
    public String getAbbreviation() { return abbreviation; }

    /**
     * Sets the short abbreviation for this unit.
     *
     * @param abbreviation the new abbreviation, or {@code null} to clear it
     */
    public void setAbbreviation(String abbreviation) { this.abbreviation = abbreviation; }

    /**
     * Returns the store that owns this unit.
     *
     * @return the owning store
     */
    public Store getStore() { return store; }

    /**
     * Sets the store that owns this unit.
     *
     * @param store the new owning store
     */
    public void setStore(Store store) { this.store = store; }
}
