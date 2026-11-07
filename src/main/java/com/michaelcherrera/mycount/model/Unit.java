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

    public Unit() {}

    public Unit(String name, String description, String abbreviation,  Store store) {
        this.name = name;
        this.description = description;
        this.abbreviation = abbreviation;
        this.store = store;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getAbbreviation() { return abbreviation; }

    public void setAbbreviation(String abbreviation) { this.abbreviation = abbreviation; }

    public Store getStore() { return store; }

    public void setStore(Store store) { this.store = store; }
}
