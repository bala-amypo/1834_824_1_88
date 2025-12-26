package com.example.demo.service.impl;

import com.example.demo.model.Booking;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingLogService;
import com.example.demo.service.BookingService;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingLogService logService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              BookingLogService logService) {
        this.bookingRepository = bookingRepository;
        this.logService = logService;
    }

    @Override
    public Booking createBooking(Long userId, Long facilityId, Booking booking) {
        booking.setUser(userId);
        booking.setFacility(facilityId);
        booking.setStatus(Booking.STATUS_CONFIRMED);

        Booking saved = bookingRepository.save(booking);
        logService.addLog(saved.getId(), "Booking created");
        return saved;
    }

    @Override
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setStatus(Booking.STATUS_CANCELLED);
        logService.addLog(bookingId, "Booking cancelled");
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }
}
