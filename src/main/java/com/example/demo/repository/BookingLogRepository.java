package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingLogRepository extends JpaRepository<BookingLog,Long>{
    List<BookingLog> findByBookingOrderByLoggedAtAsc(Booking booking);
}
