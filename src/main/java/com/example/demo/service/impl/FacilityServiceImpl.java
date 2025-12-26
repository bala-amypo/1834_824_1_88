package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.Facility;
import com.example.demo.repository.FacilityRepository;
import com.example.demo.service.FacilityService;
import java.util.List;

public class FacilityServiceImpl implements FacilityService{
    private final FacilityRepository repo;
    public FacilityServiceImpl(FacilityRepository r){repo=r;}

    public Facility addFacility(Facility f){
        if(repo.findByName(f.getName()).isPresent())
            throw new BadRequestException("exists");
        if(f.getOpenTime().compareTo(f.getCloseTime())>=0)
            throw new BadRequestException("invalid time");
        return repo.save(f);
    }

    public List<Facility> getAllFacilities(){return repo.findAll();}
}
