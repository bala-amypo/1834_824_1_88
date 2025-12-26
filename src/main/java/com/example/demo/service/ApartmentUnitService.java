package com.example.demo.service;

import com.example.demo.model.ApartmentUnit;

public interface ApartmentUnitService {
    ApartmentUnit assignUnitToUser(Long userId, ApartmentUnit unit);
    ApartmentUnit getUnitByUser(Long userId);
}
