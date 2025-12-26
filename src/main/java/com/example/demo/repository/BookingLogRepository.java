package com.example.demo.repository;

import com.example.demo.model.BookingLog;
import java.util.*;

public class BookingLogRepository {

    private final Map<Long, List<BookingLog>> store = new HashMap<>();

    public BookingLog save(BookingLog log) {
        store.computeIfAbsent(log.getBooking().getId(), k -> new ArrayList<>()).add(log);
        return log;
    }

    public List<BookingLog> findByBookingId(Long id) {
        return store.getOrDefault(id, new ArrayList<>());
    }
}
