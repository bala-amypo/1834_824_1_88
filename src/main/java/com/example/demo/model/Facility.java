package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    // Format: HH:mm
    private String openTime;

    // Format: HH:mm
    private String closeTime;

    public Facility() {
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
 
    public String getOpenTime() {
        return openTime;
    }
 
    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }
 
    public String getCloseTime() {
        return closeTime;
    }
 
    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }
}