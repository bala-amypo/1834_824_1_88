package com.example.demo.repository;

import com.example.demo.model.ApartmentUnit;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApartmentUnitRepository extends JpaRepository<ApartmentUnit, Long> {

    Optional<ApartmentUnit> findByOwner(User user);
}
