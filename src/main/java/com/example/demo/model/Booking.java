package com.example.demo.model;

import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.STATUS_CONFIRMED;

    public Booking() {}

    public Booking(Facility facility, User user,
                   LocalDateTime startTime, LocalDateTime endTime) {
        this.facility = facility;
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = BookingStatus.STATUS_CONFIRMED;
    }

    public Long getId() { return id; }
    public Facility getFacility() { return facility; }
    public User getUser() { return user; }
    public BookingStatus getStatus() { return status; }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
                