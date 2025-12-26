package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.ApartmentUnitService;
import java.util.Optional;

public class ApartmentUnitServiceImpl implements ApartmentUnitService {
    private final ApartmentUnitRepository unitRepo;
    private final UserRepository userRepo;

    public ApartmentUnitServiceImpl(ApartmentUnitRepository u, UserRepository r){
        unitRepo=u; userRepo=r;
    }

    public ApartmentUnit assignUnitToUser(Long userId, ApartmentUnit unit){
        User u=userRepo.findById(userId).get();
        unit.setOwner(u);
        return unitRepo.save(unit);
    }

    public ApartmentUnit getUnitByUser(Long id){
        User u=userRepo.findById(id).get();
        return unitRepo.findByOwner(u).get();
    }
}
