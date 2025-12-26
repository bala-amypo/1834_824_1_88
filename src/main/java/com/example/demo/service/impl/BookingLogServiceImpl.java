package com.example.demo.service.impl;

import com.example.demo.model.BookingLog;
import com.example.demo.repository.BookingLogRepository;
import com.example.demo.service.BookingLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingLogServiceImpl implements BookingLogService {

    private final BookingLogRepository repository;

    public BookingLogServiceImpl(BookingLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addLog(Long bookingId, String message) {
        BookingLog log = new BookingLog(bookingId, message, LocalDateTime.now());
        repository.save(log);
    }

    @Override
    public List<BookingLog> getLogs(Long bookingId) {
        return repository.findByBookingIdOrderByLoggedAtAsc(bookingId);
    }
}
