package com.umkolka.cgiresto.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "zones")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestoTable> restoTables;

    public Zone() {
    }

    public Zone(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RestoTable> getTables() {
        return restoTables;
    }

    public void setTables(List<RestoTable> restoTables) {
        this.restoTables = restoTables;
    }
}
