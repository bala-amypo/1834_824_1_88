package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Booking {

    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long facilityId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String status;

    public Booking() {}

    public Booking(Long userId, Long facilityId,
                   LocalDateTime startTime, LocalDateTime endTime) {
        this.userId = userId;
        this.facilityId = facilityId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = STATUS_CONFIRMED;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUser() { return userId; }
    public void setUser(Long userId) { this.userId = userId; }

    public Long getFacility() { return facilityId; }
    public void setFacility(Long facilityId) { this.facilityId = facilityId; }

    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
