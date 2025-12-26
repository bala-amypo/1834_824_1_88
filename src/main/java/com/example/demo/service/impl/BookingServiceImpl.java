package com.example.demo.service.impl;

import com.example.demo.exception.ConflictException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;

public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookRepo;
    private final FacilityRepository facRepo;
    private final UserRepository userRepo;
    private final BookingLogService logService;

    public BookingServiceImpl(BookingRepository b,FacilityRepository f,
                              UserRepository u,BookingLogService l){
        bookRepo=b; facRepo=f; userRepo=u; logService=l;
    }

    public Booking createBooking(Long fid,Long uid,Booking b){
        Facility f=facRepo.findById(fid).get();
        User u=userRepo.findById(uid).get();
        if(!bookRepo.findByFacilityAndStartTimeLessThanAndEndTimeGreaterThan(
                f,b.getEndTime(),b.getStartTime()).isEmpty())
            throw new ConflictException("conflict");
        b.setFacility(f); b.setUser(u); b.setStatus(Booking.STATUS_CONFIRMED);
        Booking saved=bookRepo.save(b);
        logService.addLog(saved.getId(),"Created");
        return saved;
    }

    public Booking cancelBooking(Long id){
        Booking b=bookRepo.findById(id).get();
        b.setStatus(Booking.STATUS_CANCELLED);
        bookRepo.save(b);
        logService.addLog(id,"Cancelled");
        return b;
    }

    public Booking getBooking(Long id){return bookRepo.findById(id).get();}
}
