package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BookingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId;
    private String message;
    private LocalDateTime loggedAt;

    public BookingLog() {}

    public BookingLog(Long bookingId, String message, LocalDateTime loggedAt) {
        this.bookingId = bookingId;
        this.message = message;
        this.loggedAt = loggedAt;
    }

    public Long getBooking() { return bookingId; }
    public String getMessage() { return message; }
    public LocalDateTime getLoggedAt() { return loggedAt; }

    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public void setMessage(String message) { this.message = message; }
}
