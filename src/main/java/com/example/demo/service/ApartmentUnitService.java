package com.example.demo.service;

import com.example.demo.model.ApartmentUnit;
import java.util.List;

public interface ApartmentUnitService {
    ApartmentUnit assignUnitToUser(Long userId, ApartmentUnit unit);
    ApartmentUnit getUnitByUser(Long userId);
    List<ApartmentUnit> getAll();
}
