package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import org.springframework.stereotype.Service;
import com.example.demo.model.BookingStatus;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;
    private final BookingLogService logService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              FacilityRepository facilityRepository,
                              UserRepository userRepository,
                              BookingLogService logService) {
        this.bookingRepository = bookingRepository;
        this.facilityRepository = facilityRepository;
        this.userRepository = userRepository;
        this.logService = logService;
    }

    @Override
    public Booking createBooking(Long userId, Long facilityId, Booking booking) {
        User user = userRepository.findById(userId).orElse(null);
        Facility facility = facilityRepository.findById(facilityId).orElse(null);

        booking.setUser(user);
        booking.setFacility(facility);
        booking.setStatus(BookingStatus.STATUS_CONFIRMED);

        Booking saved = bookingRepository.save(booking);
        logService.addLog(saved.getId(), "Booking confirmed");
        return saved;
    }

    @Override
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        booking.setStatus(BookingStatus.STATUS_CANCELLED);
        Booking saved = bookingRepository.save(booking);
        logService.addLog(saved.getId(), "Booking cancelled");
        return saved;
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }
}
