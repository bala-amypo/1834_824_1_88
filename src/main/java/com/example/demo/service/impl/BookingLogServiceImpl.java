package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.BookingLogService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingLogServiceImpl implements BookingLogService {

    private final BookingLogRepository logRepo;
    private final BookingRepository bookingRepo;

    public BookingLogServiceImpl(BookingLogRepository logRepo, BookingRepository bookingRepo) {
        this.logRepo = logRepo;
        this.bookingRepo = bookingRepo;
    }

    @Override
    public BookingLog addLog(Long bookingId, String msg) {
        Booking b = bookingRepo.findById(bookingId).orElseThrow();
        BookingLog log = new BookingLog(null,b,msg,null);
        return logRepo.save(log);
    }

    @Override
    public List<BookingLog> getLogsByBooking(Long bookingId) {
        Booking b = bookingRepo.findById(bookingId).orElseThrow();
        return logRepo.findByBookingOrderByLoggedAtAsc(b);
    }
}
