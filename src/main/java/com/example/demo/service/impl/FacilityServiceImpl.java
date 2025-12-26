package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import com.example.demo.exception.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository repo;
    public FacilityServiceImpl(FacilityRepository r){repo=r;}

    @Override
    public Facility addFacility(Facility f){
        if(f.getOpenTime().compareTo(f.getCloseTime())>=0)
            throw new BadRequestException("Invalid time");

        if(repo.findByName(f.getName()).isPresent())
            throw new ConflictException("Exists");

        return repo.save(f);
    }

    @Override
    public List<Facility> getAllFacilities(){
        return repo.findAll();
    }
}
