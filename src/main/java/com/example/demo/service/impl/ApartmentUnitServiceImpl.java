package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.ApartmentUnit;
import com.example.demo.repository.ApartmentUnitRepository;
import com.example.demo.service.ApartmentUnitService;

@Service
public class ApartmentUnitServiceImpl implements ApartmentUnitService {

    private final ApartmentUnitRepository apartmentUnitRepository;

    public ApartmentUnitServiceImpl(ApartmentUnitRepository apartmentUnitRepository) {
        this.apartmentUnitRepository = apartmentUnitRepository;
    }

    @Override
    public ApartmentUnit save(ApartmentUnit unit) {
        return apartmentUnitRepository.save(unit);
    }

    @Override
    public List<ApartmentUnit> getAll() {
        return apartmentUnitRepository.findAll();
    }
}