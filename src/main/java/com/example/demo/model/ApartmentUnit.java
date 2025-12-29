package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class ApartmentUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String unitNumber;

    @OneToOne(mappedBy = "apartmentUnit")
    private User owner;

    // -------------------
    // GETTERS & SETTERS
    // -------------------

    public Long getId() {
        return id;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
