package com.example.demo.service.impl;

import com.example.demo.model.Booking;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingLogService;
import com.example.demo.service.BookingService;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final BookingLogService logService;

    public BookingServiceImpl(BookingRepository repository, BookingLogService logService) {
        this.repository = repository;
        this.logService = logService;
    }

    @Override
    public Booking createBooking(Long facilityId, Long userId, Booking booking) {
        booking.setFacilityId(facilityId);
        booking.setUserId(userId);
        Booking saved = repository.save(booking);
        logService.addLog(saved.getId(), "Booking Created");
        return saved;
    }

    @Override
    public Booking cancelBooking(Long bookingId) {
        Booking booking = repository.findById(bookingId).orElseThrow();
        booking.setStatus("CANCELLED");
        Booking updated = repository.save(booking);
        logService.addLog(updated.getId(), "Booking Cancelled");
        return updated;
    }

    @Override
    public Booking getBooking(Long bookingId) {
        return repository.findById(bookingId).orElseThrow();
    }
}
