package com.example.demo.controller;

import com.example.demo.model.Booking;
import com.example.demo.service.BookingService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public Booking create(@RequestParam Long facilityId,
                          @RequestParam Long userId,
                          @RequestBody Booking booking) {
        return service.createBooking(facilityId, userId, booking);
    }

    @GetMapping
    public List<Booking> getAll() {
        return service.getAll();
    }
}
