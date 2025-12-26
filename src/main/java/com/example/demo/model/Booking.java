package com.example.demo.model;

import java.time.LocalDateTime;

public class Booking {

    private Long id;
    private Facility facility;
    private User user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;

    public Booking() {
        this.status = BookingStatus.STATUS_CONFIRMED;
    }

    public Booking(Long id, Facility facility, User user,
                   LocalDateTime startTime, LocalDateTime endTime,
                   BookingStatus status) {
        this.id = id;
        this.facility = facility;
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status == null ? BookingStatus.STATUS_CONFIRMED : status;
    }

    public Long getId() { return id; }
    public Facility getFacility() { return facility; }
    public User getUser() { return user; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public BookingStatus getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setFacility(Facility facility) { this.facility = facility; }
    public void setUser(User user) { this.user = user; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
