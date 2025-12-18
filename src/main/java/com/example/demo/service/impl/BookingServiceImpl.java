package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Booking;
import com.example.demo.model.BookingLog;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingLogService;
import com.example.demo.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingLogService bookingLogService;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            BookingLogService bookingLogService) {
        this.bookingRepository = bookingRepository;
        this.bookingLogService = bookingLogService;
    }

    @Override
    public Booking createBooking(Booking booking) {

        List<Booking> conflicts =
                bookingRepository.findByFacilityAndStartTimeLessThanAndEndTimeGreaterThan(
                        booking.getFacility(),
                        booking.getEndTime(),
                        booking.getStartTime()
                );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Booking conflict detected");
        }

        Booking savedBooking = bookingRepository.save(booking);

        BookingLog log = new BookingLog();
        log.setBooking(savedBooking);
        log.setLogMessage("Booking created");

        bookingLogService.save(log);

        return savedBooking;
    }
}
