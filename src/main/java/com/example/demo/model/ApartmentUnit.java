package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class ApartmentUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String unitNumber;

    // ✅ mappedBy MUST match User.apartmentUnit
    @OneToOne(mappedBy = "apartmentUnit")
    private User owner;

    // ---------------- CONSTRUCTORS ----------------

    public ApartmentUnit() {
    }

    public ApartmentUnit(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    // ---------------- GETTERS & SETTERS ----------------

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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
