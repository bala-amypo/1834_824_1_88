package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.model.Booking;
import com.example.demo.model.Facility;
import com.example.demo.model.User;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.FacilityRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BookingLogService;
import com.example.demo.service.BookingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepo;
    private final FacilityRepository facilityRepo;
    private final UserRepository userRepo;
    private final BookingLogService bookingLogService;

    // ✅ EXACT CONSTRUCTOR (TEST EXPECTED)
    public BookingServiceImpl(BookingRepository bookingRepo,
                              FacilityRepository facilityRepo,
                              UserRepository userRepo,
                              BookingLogService bookingLogService) {
        this.bookingRepo = bookingRepo;
        this.facilityRepo = facilityRepo;
        this.userRepo = userRepo;
        this.bookingLogService = bookingLogService;
    }

    @Override
    public Booking createBooking(Long facilityId, Long userId, Booking booking) {

        Facility facility = facilityRepo.findById(facilityId)
                .orElseThrow(() -> new BadRequestException("Facility not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        List<Booking> conflicts =
                bookingRepo.findByFacilityAndStartTimeLessThanAndEndTimeGreaterThan(
                        facility, booking.getEndTime(), booking.getStartTime());

        if (!conflicts.isEmpty()) {
            throw new ConflictException("Booking conflict");
        }

        booking.setFacility(facility);
        booking.setUser(user);
        booking.setStatus(Booking.STATUS_CONFIRMED);

        Booking saved = bookingRepo.save(booking);
        bookingLogService.addLog(saved.getId(), "Booking Created");
        return saved;
    }

    @Override
    public Booking cancelBooking(Long id) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new BadRequestException("Booking not found"));

        booking.setStatus(Booking.STATUS_CANCELLED);
        Booking saved = bookingRepo.save(booking);
        bookingLogService.addLog(saved.getId(), "Booking Cancelled");
        return saved;
    }

    @Override
    public Booking getBooking(Long id) {
        return bookingRepo.findById(id).orElseThrow();
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepo.findAll();
    }
}
