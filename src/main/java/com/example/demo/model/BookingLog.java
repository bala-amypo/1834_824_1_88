package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BookingLog {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Booking booking;

    private String logMessage;

    private LocalDateTime loggedAt;

    public BookingLog() {}

    public BookingLog(Long id, Booking booking, String msg, LocalDateTime time) {
        this.id = id;
        this.booking = booking;
        this.logMessage = msg;
        this.loggedAt = time;
    }

    @PrePersist
    public void onCreate() {
        if (loggedAt == null) loggedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Booking getBooking() { return booking; }
    public String getLogMessage() { return logMessage; }
    public LocalDateTime getLoggedAt() { return loggedAt; }

    public void setLogMessage(String msg) { this.logMessage = msg; }
}
