package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ApartmentUnit;
import com.example.demo.service.ApartmentUnitService;

@RestController
@RequestMapping("/units")
public class ApartmentUnitController {

    private final ApartmentUnitService apartmentUnitService;

    public ApartmentUnitController(ApartmentUnitService apartmentUnitService) {
        this.apartmentUnitService = apartmentUnitService;
    }

    @PostMapping
    public ApartmentUnit createUnit(@RequestBody ApartmentUnit unit) {
        return apartmentUnitService.save(unit);
    }

    @GetMapping
    public List<ApartmentUnit> getAllUnits() {
        return apartmentUnitService.getAll();
    }
}