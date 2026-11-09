package com.michaelcherrera.mycount.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Represents a unit of measurement available for a specific item at a specific
 * location. Stores the conversion factor relative to the item's base unit and
 * indicates whether the unit is used for prepared inventory.
 */
@Entity
@Table(name = "item_location_units", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "item_location_id", "unit_id" })
})
public class ItemLocationUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_location_id", nullable = false)
    private ItemLocation itemLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @Column(nullable = false)
    private BigDecimal conversionFactor;

    @Column(nullable = false)
    private boolean prepUnit = false;

    /**
     * Default constructor required by JPA.
     */
    public ItemLocationUnit() {}

    /**
     * Constructs an ItemLocationUnit with the specified attributes.
     *
     * @param itemLocation the item-location association this unit belongs to
     * @param unit the unit of measurement
     * @param conversionFactor the factor to convert this unit to the item's base unit
     * @param prepUnit {@code true} if this unit is used for prepared inventory
     */
    public ItemLocationUnit(ItemLocation itemLocation, Unit unit, BigDecimal conversionFactor, boolean prepUnit) {
        this.itemLocation = itemLocation;
        this.unit = unit;
        this.conversionFactor = conversionFactor;
        this.prepUnit = prepUnit;
    }

    /**
     * Returns the unique identifier for this item-location-unit entry.
     *
     * @return the ID, or {@code null} if not yet persisted
     */
    public Long getId() { return id; }

    /**
     * Returns the item-location association for this unit entry.
     *
     * @return the parent {@link ItemLocation}
     */
    public ItemLocation getItemLocation() { return itemLocation; }

    /**
     * Sets the item-location association for this unit entry.
     *
     * @param itemLocation the parent {@link ItemLocation}
     */
    public void setItemLocation(ItemLocation itemLocation) { this.itemLocation = itemLocation; }

    /**
     * Returns the unit of measurement for this entry.
     *
     * @return the unit
     */
    public Unit getUnit() { return unit; }

    /**
     * Sets the unit of measurement for this entry.
     *
     * @param unit the unit
     */
    public void setUnit(Unit unit) { this.unit = unit; }

    /**
     * Returns the conversion factor used to convert quantities in this unit
     * to the item's base unit.
     *
     * @return the conversion factor as a {@link BigDecimal}
     */
    public BigDecimal getConversionFactor() { return conversionFactor; }

    /**
     * Sets the conversion factor for this unit entry.
     *
     * @param conversionFactor the new conversion factor
     */
    public void setConversionFactor(BigDecimal conversionFactor) { this.conversionFactor = conversionFactor; }

    /**
     * Returns whether this unit is used for prepared inventory.
     *
     * @return {@code true} if this is a prep unit
     */
    public boolean isPrepUnit() { return prepUnit; }

    /**
     * Sets whether this unit is used for prepared inventory.
     *
     * @param prepUnit {@code true} to mark as a prep unit
     */
    public void setPrepUnit(boolean prepUnit) { this.prepUnit = prepUnit; }
}
