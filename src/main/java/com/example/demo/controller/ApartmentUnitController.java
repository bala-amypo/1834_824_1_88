package com.example.demo.controller;

import com.example.demo.model.ApartmentUnit;
import com.example.demo.service.ApartmentUnitService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/units")
public class ApartmentUnitController {

    private final ApartmentUnitService service;

    public ApartmentUnitController(ApartmentUnitService service) {
        this.service = service;
    }

    // Assign unit to user
    @PostMapping("/{userId}")
    public ApartmentUnit assignUnit(@PathVariable Long userId,
                                    @RequestBody ApartmentUnit unit) {
        return service.assignUnitToUser(userId, unit);
    }

    // Get unit by user
    @GetMapping("/{userId}")
    public ApartmentUnit getUnitByUser(@PathVariable Long userId) {
        return service.getUnitByUser(userId);
    }
}
