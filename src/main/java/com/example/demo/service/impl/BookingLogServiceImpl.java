package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingLogServiceImpl implements BookingLogService {

    private final BookingLogRepository repo;
    private final BookingRepository bookingRepo;

    public BookingLogServiceImpl(BookingLogRepository r, BookingRepository b){
        this.repo=r; this.bookingRepo=b;
    }

    @Override
    public BookingLog addLog(Long bookingId,String msg){
        Booking b = bookingRepo.findById(bookingId).orElseThrow();
        BookingLog log = new BookingLog(null,b,msg,null);
        return repo.save(log);
    }

    @Override
    public List<BookingLog> getLogsByBooking(Long id){
        Booking b = bookingRepo.findById(id).orElseThrow();
        return repo.findByBookingOrderByLoggedAtAsc(b);
    }
}
