package com.example.demo.repository;

import java.time.LocalDateTime;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Booking;
import com.example.demo.model.Facility;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByFacilityAndStartTimeLessThanAndEndTimeGreaterThan(
            Facility facility,
            LocalDateTime endTime,
            LocalDateTime startTime
    );
}