package com.example.demo.service.impl;

import com.example.demo.model.Booking;
import com.example.demo.model.BookingLog;
import com.example.demo.repository.BookingLogRepository;
import com.example.demo.service.BookingLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingLogServiceImpl implements BookingLogService {

    private final BookingLogRepository repository = new BookingLogRepository();

    @Override
    public BookingLog logBooking(Long bookingId, String message) {
        Booking b = new Booking();
        b.setId(bookingId);

        BookingLog log = new BookingLog();
        log.setBooking(b);
        log.setMessage(message);
        log.setLoggedAt(LocalDateTime.now());

        return repository.save(log);
    }

    @Override
    public List<BookingLog> getLogsByBooking(Long bookingId) {
        return repository.findByBookingId(bookingId);
    }
}
