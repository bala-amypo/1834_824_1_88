package com.example.demo.controller;

import com.example.demo.model.Facility;
import com.example.demo.service.FacilityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facilities")
public class FacilityController {

    private final FacilityService service;

    public FacilityController(FacilityService service) {
        this.service = service;
    }

    @PostMapping
    public Facility save(@RequestBody Facility facility) {
        return service.addFacility(facility);
    }

    @GetMapping
    public List<Facility> getAll() {
        return service.getAllFacilities();
    }
}
