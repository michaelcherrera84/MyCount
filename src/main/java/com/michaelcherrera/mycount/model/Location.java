package com.michaelcherrera.mycount.model;

import jakarta.persistence.*;

/**
 * Represents a physical storage or inventory location where items are tracked.
 * Examples include dry storage, coolers, freezers, and makelines.
 */
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public Location() {}

    public Location(String name) {
        this.name = name;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
