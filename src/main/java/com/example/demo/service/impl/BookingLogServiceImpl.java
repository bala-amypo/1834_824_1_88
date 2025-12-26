package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.BookingLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingLogServiceImpl implements BookingLogService {

    private final BookingLogRepository repository;
    private final BookingRepository bookingRepository;

    public BookingLogServiceImpl(BookingLogRepository repository,
                                 BookingRepository bookingRepository) {
        this.repository = repository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingLog addLog(Long bookingId, String message) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        BookingLog log = new BookingLog(null, booking, message, LocalDateTime.now());
        return repository.save(log);
    }

    @Override
    public List<BookingLog> getLogsByBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        return repository.findByBookingOrderByLoggedAtAsc(booking);
    }
}
