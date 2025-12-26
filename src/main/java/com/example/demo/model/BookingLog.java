package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BookingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private String logMessage;

    private LocalDateTime loggedAt;

    public BookingLog() {}

    public BookingLog(Long id, Booking booking, String logMessage, LocalDateTime loggedAt) {
        this.id = id;
        this.booking = booking;
        this.logMessage = logMessage;
        this.loggedAt = loggedAt;
    }

    @PrePersist
    public void onCreate() {
        this.loggedAt = LocalDateTime.now();
    }

    // ---------------- GETTERS ----------------

    public Long getId() {
        return id;
    }

    public Booking getBooking() {
        return booking;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }

    // ---------------- SETTERS ----------------

    public void setId(Long id) {
        this.id = id;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public void setLoggedAt(LocalDateTime loggedAt) {
        this.loggedAt = loggedAt;
    }
}
