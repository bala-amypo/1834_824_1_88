package com.example.demo.controller;

import com.example.demo.model.BookingLog;
import com.example.demo.service.BookingLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking-logs")
public class BookingLogController {

    private final BookingLogService service;

    public BookingLogController(BookingLogService service) {
        this.service = service;
    }

    @GetMapping("/{bookingId}")
    public List<BookingLog> getLogs(@PathVariable Long bookingId) {
        return service.getLogsByBooking(bookingId);
    }
}
