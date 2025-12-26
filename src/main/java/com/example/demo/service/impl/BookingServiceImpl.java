package com.example.demo.service.impl;

import com.example.demo.exception.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepo;
    private final FacilityRepository facilityRepo;
    private final UserRepository userRepo;
    private final BookingLogService logService;

    public BookingServiceImpl(BookingRepository b, FacilityRepository f, UserRepository u, BookingLogService l) {
        bookingRepo = b; facilityRepo = f; userRepo = u; logService = l;
    }

    @Override
    public Booking createBooking(Long facilityId, Long userId, Booking booking) {
        Facility fac = facilityRepo.findById(facilityId).orElseThrow();
        User u = userRepo.findById(userId).orElseThrow();

        List<Booking> conflicts =
            bookingRepo.findByFacilityAndStartTimeLessThanAndEndTimeGreaterThan(
                    fac, booking.getEndTime(), booking.getStartTime());

        if(!conflicts.isEmpty()) throw new ConflictException("Slot busy");

        booking.setFacility(fac);
        booking.setUser(u);
        booking.setStatus(Booking.STATUS_CONFIRMED);

        Booking saved = bookingRepo.save(booking);
        logService.addLog(saved.getId(),"Created");
        return saved;
    }

    @Override
    public Booking cancelBooking(Long id) {
        Booking b = bookingRepo.findById(id).orElseThrow();
        b.setStatus(Booking.STATUS_CANCELLED);
        Booking saved = bookingRepo.save(b);
        logService.addLog(id,"Cancelled");
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
