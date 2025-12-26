package com.example.demo.service.impl;

import com.example.demo.model.Booking;
import com.example.demo.model.BookingLog;
import com.example.demo.repository.BookingLogRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingLogService;

import java.util.List;

@Service
public class BookingLogServiceImpl implements BookingLogService {

    private final BookingLogRepository logRepo;
    private final BookingRepository bookingRepo;

    public BookingLogServiceImpl(BookingLogRepository logRepo,
                                 BookingRepository bookingRepo) {
        this.logRepo = logRepo;
        this.bookingRepo = bookingRepo;
    }

    @Override
    public BookingLog addLog(Long bookingId, String msg) {
        Booking booking = bookingRepo.findById(bookingId).get();
        BookingLog log = new BookingLog(null, booking, msg, null);
        return logRepo.save(log);
    }

    @Override
    public List<BookingLog> getLogsByBooking(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId).get();
        return logRepo.findByBookingOrderByLoggedAtAsc(booking);
    }

    @Override
    public BookingLog save(BookingLog log) {
        return logRepo.save(log);
    }
}
