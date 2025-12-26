package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.BookingLogService;
import java.util.List;

public class BookingLogServiceImpl implements BookingLogService {
    private final BookingLogRepository logRepo;
    private final BookingRepository bookRepo;

    public BookingLogServiceImpl(BookingLogRepository l,BookingRepository b){
        logRepo=l; bookRepo=b;
    }

    public BookingLog addLog(Long bookingId,String msg){
        Booking b=bookRepo.findById(bookingId).get();
        return logRepo.save(new BookingLog(null,b,msg,null));
    }

    public List<BookingLog> getLogsByBooking(Long id){
        return logRepo.findByBookingOrderByLoggedAtAsc(bookRepo.findById(id).get());
    }
}
