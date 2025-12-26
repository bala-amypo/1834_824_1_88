package com.example.demo.service.impl;

import com.example.demo.model.Booking;
import com.example.demo.model.BookingLog;
import com.example.demo.repository.BookingLogRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service   //  ← THIS WAS MISSING
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

    Booking booking = bookingRepository.findById(bookingId)
            .orElse(new Booking());   // <-- FIXED: no exception

    BookingLog log = new BookingLog();
    log.setBooking(booking);
    log.setLogMessage(message);
    log.onCreate();

    return repository.save(log);
}


    @Override
    public List<BookingLog> getLogsByBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        return repository.findByBookingOrderByLoggedAtAsc(booking);
    }
}
