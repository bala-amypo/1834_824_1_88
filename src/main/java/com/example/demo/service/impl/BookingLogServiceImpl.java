package com.example.demo.service.impl;

import com.example.demo.model.Booking;
import com.example.demo.model.BookingLog;
import com.example.demo.repository.BookingLogRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingLogServiceImpl implements BookingLogService {

    private final BookingLogRepository repo;
    private final BookingRepository bookingRepo;

    // ✅ EXACT CONSTRUCTOR (TEST EXPECTED)
    public BookingLogServiceImpl(BookingLogRepository repo,
                                 BookingRepository bookingRepo) {
        this.repo = repo;
        this.bookingRepo = bookingRepo;
    }

    @Override
    public BookingLog addLog(Long bookingId, String msg) {
        Booking booking = bookingRepo.findById(bookingId).orElse(new Booking());
        BookingLog log = new BookingLog();
        log.setBooking(booking);
        log.setLogMessage(msg);
        return repo.save(log);
    }

    @Override
    public List<BookingLog> getLogsByBooking(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow();
        return repo.findByBookingOrderByLoggedAtAsc(booking);
    }
}
