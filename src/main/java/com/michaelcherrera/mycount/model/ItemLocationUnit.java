package com.michaelcherrera.mycount.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

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

    public ItemLocationUnit() {}

    public ItemLocationUnit(ItemLocation itemLocation, Unit unit, BigDecimal conversionFactor, boolean prepUnit) {
        this.itemLocation = itemLocation;
        this.unit = unit;
        this.conversionFactor = conversionFactor;
        this.prepUnit = prepUnit;
    }

    public Long getId() { return id; }

    public ItemLocation getItemLocation() { return itemLocation; }

    public void setItemLocation(ItemLocation itemLocation) { this.itemLocation = itemLocation; }

    public Unit getUnit() { return unit; }

    public void setUnit(Unit unit) { this.unit = unit; }

    public BigDecimal getConversionFactor() { return conversionFactor; }

    public void setConversionFactor(BigDecimal conversionFactor) { this.conversionFactor = conversionFactor; }

    public boolean isPrepUnit() { return prepUnit; }

    public void setPrepUnit(boolean prepUnit) { this.prepUnit = prepUnit; }
}
