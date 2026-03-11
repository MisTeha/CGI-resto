package com.umkolka.cgiresto.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

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
        this.startTime = start;
        this.endTime = end;
        this.restoTable = restoTable;
        this.numberOfPeople = numberOfPeople;
    }

    public Reservation(LocalDateTime start, LocalDateTime end, RestoTable restoTable, Integer numberOfPeople,
            String customerName, String customerPhone) {
        this.startTime = start;
        this.endTime = end;
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
        return startTime;
    }

    public void setStart(LocalDateTime start) {
        this.startTime = start;
    }

    public LocalDateTime getEnd() {
        return endTime;
    }

    public void setEnd(LocalDateTime end) {
        this.endTime = end;
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
