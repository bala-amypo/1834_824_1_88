package com.example.demo.service.impl;

import com.example.demo.model.Booking;
import com.example.demo.model.BookingLog;
import com.example.demo.repository.BookingLogRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingLogRepository logRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              BookingLogRepository logRepository) {
        this.bookingRepository = bookingRepository;
        this.logRepository = logRepository;
    }

    @Override
    public Booking createBooking(Long facilityId, Long userId, Booking booking) {
        booking.setFacilityId(facilityId);
        booking.setUserId(userId);
        booking.setStatus("CREATED");

        Booking saved = bookingRepository.save(booking);

        BookingLog log = new BookingLog();
        log.setBookingId(saved.getId());
        log.setMessage("Booking created");
        logRepository.save(log);

        return saved;
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }
}
