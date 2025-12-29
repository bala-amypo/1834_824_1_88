package com.example.demo.controller;

import com.example.demo.model.ApartmentUnit;
import com.example.demo.service.ApartmentUnitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/units")
public class ApartmentUnitController {

    private final ApartmentUnitService service;

    public ApartmentUnitController(ApartmentUnitService service) {
        this.service = service;
    }

    @PostMapping("/{userId}")
    public ApartmentUnit assignUnit(@PathVariable Long userId,
                                    @RequestBody ApartmentUnit unit) {
        return service.assignUnitToUser(userId, unit);
    }

    @GetMapping
    public List<ApartmentUnit> getAllUnits() {
        return service.getAll();
    }

    @GetMapping("/user/{userId}")
    public ApartmentUnit getByUser(@PathVariable Long userId) {
        return service.getUnitByUser(userId);
    }
}
