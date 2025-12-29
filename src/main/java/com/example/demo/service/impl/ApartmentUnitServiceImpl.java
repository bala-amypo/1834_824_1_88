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

    private final ApartmentUnitRepository unitRepository;
    private final UserRepository userRepository;

    public ApartmentUnitServiceImpl(ApartmentUnitRepository unitRepository,
                                    UserRepository userRepository) {
        this.unitRepository = unitRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ApartmentUnit assignUnitToUser(Long userId, ApartmentUnit unit) {
        User user = userRepository.findById(userId).orElseThrow();
        unit.setOwner(user);
        return unitRepository.save(unit);
    }

    @Override
    public ApartmentUnit getUnitByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return unitRepository.findByOwner(user).orElseThrow();
    }

    @Override
    public List<ApartmentUnit> getAll() {
        return unitRepository.findAll();
    }

    @Override
    public ApartmentUnit save(ApartmentUnit unit) {
        return unitRepository.save(unit);
    }
}
