package com.example.demo.service.impl;

import com.example.demo.model.ApartmentUnit;
import com.example.demo.model.User;
import com.example.demo.repository.ApartmentUnitRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ApartmentUnitService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApartmentUnitServiceImpl implements ApartmentUnitService {

    private final ApartmentUnitRepository unitRepo;
    private final UserRepository userRepo;

    public ApartmentUnitServiceImpl(ApartmentUnitRepository unitRepo,
                                    UserRepository userRepo) {
        this.unitRepo = unitRepo;
        this.userRepo = userRepo;
    }

    @Override
    public ApartmentUnit assignUnitToUser(Long userId, ApartmentUnit unit) {
        User user = userRepo.findById(userId).get();
        unit.setOwner(user);
        return unitRepo.save(unit);
    }

    @Override
    public ApartmentUnit getUnitByUser(Long userId) {
        User user = userRepo.findById(userId).get();
        return unitRepo.findByOwner(user).get();
    }

    @Override
    public List<ApartmentUnit> getAll() {
        return unitRepo.findAll();
    }
    @Override
public ApartmentUnit save(ApartmentUnit unit) {
    return unitRepo.save(unit);
}

}
