package com.example.demo.service;

import com.example.demo.model.BookingLog;
import java.util.List;

public interface BookingLogService {
    BookingLog addLog(Long bookingId, String msg);
    List<BookingLog> getLogsByBooking(Long bookingId);
}
