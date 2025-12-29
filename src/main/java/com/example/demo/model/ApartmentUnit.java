package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class ApartmentUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String unitNumber;

    private Integer floor;

    @OneToOne
    private User owner;

    public ApartmentUnit() {}

    public ApartmentUnit(Long id, String unitNumber, Integer floor, User owner) {
        this.id = id;
        this.unitNumber = unitNumber;
        this.floor = floor;
        this.owner = owner;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUnitNumber() { return unitNumber; }
    public void setUnitNumber(String unitNumber) { this.unitNumber = unitNumber; }

    public Integer getFloor() { return floor; }
    public void setFloor(Integer floor) { this.floor = floor; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
}
