package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Booking {

    public static final BookingStatus STATUS_CONFIRMED = BookingStatus.CONFIRMED;
    public static final BookingStatus STATUS_CANCELLED = BookingStatus.CANCELLED;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Facility facility;

    @ManyToOne
    private User user;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = STATUS_CONFIRMED;

    public Booking() {}

    public Booking(Long id, Facility facility, User user,
                   LocalDateTime startTime, LocalDateTime endTime,
                   BookingStatus status) {
        this.id = id;
        this.facility = facility;
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status == null ? STATUS_CONFIRMED : status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Facility getFacility() { return facility; }
    public void setFacility(Facility facility) { this.facility = facility; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
