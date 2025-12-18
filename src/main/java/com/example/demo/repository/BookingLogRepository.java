package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.BookingLog;

public interface BookingLogRepository extends JpaRepository<BookingLog, Long> {
    
}