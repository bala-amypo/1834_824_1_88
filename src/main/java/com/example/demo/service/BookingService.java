package com.example.demo.service;

import com.example.demo.model.Booking;

public interface BookingService {

    Booking createBooking(Long userId, Long facilityId, Booking booking);

    Booking cancelBooking(Long bookingId);

    Booking getBooking(Long bookingId);
}
