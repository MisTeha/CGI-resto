package com.umkolka.cgiresto.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@jakarta.persistence.Table(name = "resto_tables")
public class RestoTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(nullable = false)
    private Integer nSeats;

    @Column(nullable = false)
    private Boolean isNextToWindow;

    @Column(name = "table_number")
    private String tableNumber;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    public RestoTable() {
    }

    public RestoTable(Zone zone, Integer nSeats, Boolean isNextToWindow) {
        this.zone = zone;
        this.nSeats = nSeats;
        this.isNextToWindow = isNextToWindow;
    }

    public RestoTable(Zone zone, Integer nSeats, Boolean isNextToWindow, String tableNumber) {
        this.zone = zone;
        this.nSeats = nSeats;
        this.isNextToWindow = isNextToWindow;
        this.tableNumber = tableNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Integer getNSeats() {
        return nSeats;
    }

    public void setNSeats(Integer nSeats) {
        this.nSeats = nSeats;
    }

    public Boolean getIsNextToWindow() {
        return isNextToWindow;
    }

    public void setIsNextToWindow(Boolean isNextToWindow) {
        this.isNextToWindow = isNextToWindow;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
