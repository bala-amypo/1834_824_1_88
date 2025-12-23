package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;

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

    public ApartmentUnit() {
    }

    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public String getUnitNumber() {
        return unitNumber;
    }
 
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }
 
    public Integer getFloor() {
        return floor;
    }
 
    public void setFloor(Integer floor) {
        this.floor = floor;
    }
 
    public User getOwner() {
        return owner;
    }
 
    public void setOwner(User owner) {
        this.owner = owner;
    }
}