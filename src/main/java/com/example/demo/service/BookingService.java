package com.example.demo.service;

import com.example.demo.model.Booking;
import java.util.List;

public interface BookingService {

    Booking createBooking(Long facilityId, Long userId, Booking booking);

    Booking cancelBooking(Long id);

    Booking getBooking(Long id);

    List<Booking> getAll();   // <-- THIS was missing
}
