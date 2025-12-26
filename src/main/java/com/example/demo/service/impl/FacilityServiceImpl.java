package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.Facility;
import com.example.demo.repository.FacilityRepository;
import com.example.demo.service.FacilityService;

import java.util.List;

@Service
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository repo;

    public FacilityServiceImpl(FacilityRepository repo) {
        this.repo = repo;
    }

    @Override
    public Facility addFacility(Facility f) {
        if (repo.findByName(f.getName()).isPresent()) {
            throw new BadRequestException("Facility already exists");
        }
        if (f.getOpenTime().compareTo(f.getCloseTime()) >= 0) {
            throw new BadRequestException("Invalid time");
        }
        return repo.save(f);
    }

    @Override
    public List<Facility> getAllFacilities() {
        return repo.findAll();
    }

    @Override
    public Facility save(Facility f) {
        return repo.save(f);
    }
}
