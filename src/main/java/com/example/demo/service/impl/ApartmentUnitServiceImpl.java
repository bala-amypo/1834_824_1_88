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

    private final ApartmentUnitRepository repo;
    private final UserRepository userRepo;

    // ✅ EXACT CONSTRUCTOR (TEST EXPECTED)
    public ApartmentUnitServiceImpl(ApartmentUnitRepository repo,
                                    UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    @Override
    public ApartmentUnit assignUnitToUser(Long userId, ApartmentUnit unit) {
        User user = userRepo.findById(userId).orElseThrow();
        unit.setOwner(user);
        ApartmentUnit saved = repo.save(unit);
        user.setApartmentUnit(saved);
        return saved;
    }

    @Override
    public ApartmentUnit getUnitByUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        return repo.findByOwner(user).orElse(null);
    }

    @Override
    public List<ApartmentUnit> getAll() {
        return repo.findAll();
    }

    @Override
    public ApartmentUnit save(ApartmentUnit unit) {
        return repo.save(unit);
    }
}
