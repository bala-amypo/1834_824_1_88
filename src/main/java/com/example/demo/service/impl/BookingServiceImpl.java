package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.model.Booking;
import com.example.demo.model.BookingStatus;
import com.example.demo.model.Facility;
import com.example.demo.model.User;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.FacilityRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BookingLogService;
import com.example.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;
    private final BookingLogService bookingLogService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              FacilityRepository facilityRepository,
                              UserRepository userRepository,
                              BookingLogService bookingLogService) {
        this.bookingRepository = bookingRepository;
        this.facilityRepository = facilityRepository;
        this.userRepository = userRepository;
        this.bookingLogService = bookingLogService;
    }

    @Override
    public Booking createBooking(Long userId, Long facilityId, Booking booking) {

        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new BadRequestException("Facility not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        List<Booking> conflicts =
                bookingRepository.findByFacilityAndStartTimeLessThanAndEndTimeGreaterThan(
                        facility, booking.getEndTime(), booking.getStartTime());

        if (!conflicts.isEmpty()) {
            throw new ConflictException("Facility already booked for given slot");
        }

        booking.setFacility(facility);
        booking.setUser(user);
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking saved = bookingRepository.save(booking);

        bookingLogService.addLog(saved.getId(), "Booking Created");

        return saved;
    }

    @Override
    public Booking cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BadRequestException("Booking not found"));

        booking.setStatus(BookingStatus.CANCELLED);

        Booking saved = bookingRepository.save(booking);

        bookingLogService.addLog(saved.getId(), "Booking Cancelled");

        return saved;
    }

    @Override
    public Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Booking not found"));
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }
}
