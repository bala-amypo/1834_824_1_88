package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import com.example.demo.exception.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepo;
    private final FacilityRepository facilityRepo;
    private final UserRepository userRepo;
    private final BookingLogService logService;

    public BookingServiceImpl(BookingRepository b,FacilityRepository f,UserRepository u,BookingLogService l){
        bookingRepo=b; facilityRepo=f; userRepo=u; logService=l;
    }

    @Override
    public Booking createBooking(Long fid,Long uid,Booking b){
        Facility f=facilityRepo.findById(fid).orElseThrow();
        User u=userRepo.findById(uid).orElseThrow();

        List<Booking> conflicts =
                bookingRepo.findByFacilityAndStartTimeLessThanAndEndTimeGreaterThan(
                        f,b.getEndTime(),b.getStartTime());

        if(!conflicts.isEmpty()) throw new ConflictException("Conflict");

        b.setFacility(f);
        b.setUser(u);
        b.setStatus(BookingStatus.STATUS_CONFIRMED);

        Booking saved=bookingRepo.save(b);
        logService.addLog(saved.getId(),"Created");
        return saved;
    }

    @Override
    public Booking cancelBooking(Long id){
        Booking b=bookingRepo.findById(id).orElseThrow();
        b.setStatus(BookingStatus.STATUS_CANCELLED);
        bookingRepo.save(b);
        logService.addLog(id,"Cancelled");
        return b;
    }

    @Override
    public Booking getBooking(Long id){
        return bookingRepo.findById(id).orElseThrow();
    }

    @Override
    public List<Booking> getAll(){ return bookingRepo.findAll(); }
}
