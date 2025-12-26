package com.example.demo.service.impl;

import com.example.demo.model.Facility;
import com.example.demo.repository.FacilityRepository;
import com.example.demo.service.FacilityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository repository;

    public FacilityServiceImpl(FacilityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Facility addFacility(Facility facility) {
        return repository.save(facility);
    }

    @Override
    public List<Facility> getAllFacilities() {
        return repository.findAll();
    }
}
