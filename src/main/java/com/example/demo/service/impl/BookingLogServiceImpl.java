package com.example.demo.service.impl;

import com.example.demo.model.BookingLog;
import com.example.demo.repository.BookingLogRepository;
import com.example.demo.service.BookingLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingLogServiceImpl implements BookingLogService {

    private final BookingLogRepository repository;

    public BookingLogServiceImpl(BookingLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public BookingLog addLog(Long bookingId, String message) {
        BookingLog log = new BookingLog();
        log.setBookingId(bookingId);
        log.setMessage(message);
        return repository.save(log);
    }

    @Override
    public List<BookingLog> getLogsByBooking(Long bookingId) {
        return repository.findByBookingId(bookingId);
    }
}
