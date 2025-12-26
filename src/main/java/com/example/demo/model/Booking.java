package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Facility facility;

    @ManyToOne
    private User user;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String status;

    public Booking() {
    }

    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public Facility getFacility() {
        return facility;
    }
 
    public void setFacility(Facility facility) {
        this.facility = facility;
    }
 
    public User getUser() {
        return user;
    }
 
    public void setUser(User user) {
        this.user = user;
    }
 
    public LocalDateTime getStartTime() {
        return startTime;
    }
 
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
 
    public LocalDateTime getEndTime() {
        return endTime;
    }
 
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
 
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }
}