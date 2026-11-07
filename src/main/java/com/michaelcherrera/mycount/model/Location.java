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

    public Location() {}

    public Location(String name, Store store) {
        this.name = name;
        this.store = store;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Store getStore() { return store; }

    public void setStore(Store store) { this.store = store; }
}
