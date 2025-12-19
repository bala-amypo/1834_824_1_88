package com.example.demo.service;

import java.util.List;

import com.example.demo.model.ApartmentUnit;

public interface ApartmentUnitService {

    ApartmentUnit save(ApartmentUnit unit);

    List<ApartmentUnit> getAll();
}