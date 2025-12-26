package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Booking {

    public static final BookingStatus STATUS_CONFIRMED = BookingStatus.STATUS_CONFIRMED;
    public static final BookingStatus STATUS_CANCELLED = BookingStatus.STATUS_CANCELLED;

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
    private BookingStatus status = BookingStatus.STATUS_CONFIRMED;

    public Booking(){}

    public Booking(Long id, Facility f, User u, LocalDateTime s, LocalDateTime e, BookingStatus st){
        this.id=id; this.facility=f; this.user=u;
        this.startTime=s; this.endTime=e;
        this.status = st==null ? BookingStatus.STATUS_CONFIRMED : st;
    }

    // getters setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public Facility getFacility(){return facility;}
    public void setFacility(Facility f){this.facility=f;}
    public User getUser(){return user;}
    public void setUser(User u){this.user=u;}
    public LocalDateTime getStartTime(){return startTime;}
    public void setStartTime(LocalDateTime s){this.startTime=s;}
    public LocalDateTime getEndTime(){return endTime;}
    public void setEndTime(LocalDateTime e){this.endTime=e;}
    public BookingStatus getStatus(){return status;}
    public void setStatus(BookingStatus s){this.status=s;}
}
