package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ApartmentUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String unitNumber;

    private Integer floor;

    @OneToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties({"apartmentUnit"}) // ✅ stop loop
    private User owner;

    // ---------- getters & setters ----------

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
