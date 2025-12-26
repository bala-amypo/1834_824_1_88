package com.example.demo.service;

import com.example.demo.model.BookingLog;
import java.util.List;

public interface BookingLogService {
    void addLog(Long bookingId, String message);
    List<BookingLog> getLogsByBooking(Long bookingId);
}
