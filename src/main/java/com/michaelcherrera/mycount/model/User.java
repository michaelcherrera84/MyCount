package com.michaelcherrera.mycount.model;

import jakarta.persistence.*;

import java.util.*;

/**
 * This class encapsulates the details of a user, including their unique identifier,
 * username, email, password, and relationships with stores and other users. It provides
 * methods to manage these attributes and is mapped to the "users" table in the database.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private final Set<Store> stores = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_supervision",
            joinColumns = @JoinColumn(name = "supervisor_id"),
            inverseJoinColumns = @JoinColumn(name = "subordinate_id")
    )
    private final Set<User> subordinates = new HashSet<>();

    @ManyToMany(mappedBy = "subordinates")
    private final Set<User> supervisors = new HashSet<>();

    @ManyToMany(mappedBy = "accessors")
    private final Set<Store> accessibleStores = new HashSet<>();

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UUID getId() { return id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Set<Store> getStores() { return stores; }

    public void addStore(Store store) {
        this.stores.add(store);
        store.setOwner(this);
        store.addAccessor(this);
    }

    public Set<User> getSubordinates() { return subordinates; }

    public void addSubordinate(User subordinate) {
        this.subordinates.add(subordinate);
        subordinate.getSupervisors().add(this);
    }

    public void removeSubordinate(User subordinate) {
        this.subordinates.remove(subordinate);
        subordinate.getSupervisors().remove(this);
    }

    public Set<User> getSupervisors() { return supervisors; }

    public void addSupervisor(User supervisor) {
        this.supervisors.add(supervisor);
        supervisor.getSubordinates().add(this);
    }

    public void removeSupervisor(User supervisor) {
        this.supervisors.remove(supervisor);
        supervisor.getSubordinates().remove(this);
    }

    public Set<Store> getAccessibleStores() { return accessibleStores; }

    public void addAccessibleStore(Store store) {
        this.accessibleStores.add(store);
        store.addAccessor(this);
    }

    public void removeAccessibleStore(Store store) {
        this.accessibleStores.remove(store);
        store.getAccessors().remove(this);
    }
}
