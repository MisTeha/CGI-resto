package com.umkolka.cgiresto.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime start;

    @Column(nullable = false)
    private LocalDateTime end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private RestoTable restoTable;

    @Column(nullable = false)
    private Integer numberOfPeople;

    @Column(length = 255)
    private String customerName;

    @Column(length = 20)
    private String customerPhone;

    public Reservation() {
    }

    public Reservation(LocalDateTime start, LocalDateTime end, RestoTable restoTable, Integer numberOfPeople) {
        this.start = start;
        this.end = end;
        this.restoTable = restoTable;
        this.numberOfPeople = numberOfPeople;
    }

    public Reservation(LocalDateTime start, LocalDateTime end, RestoTable restoTable, Integer numberOfPeople,
                       String customerName, String customerPhone) {
        this.start = start;
        this.end = end;
        this.restoTable = restoTable;
        this.numberOfPeople = numberOfPeople;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public RestoTable getTable() {
        return restoTable;
    }

    public void setTable(RestoTable restoTable) {
        this.restoTable = restoTable;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}
