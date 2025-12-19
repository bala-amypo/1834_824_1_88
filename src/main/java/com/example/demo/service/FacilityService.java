package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Facility;

public interface FacilityService {

    Facility save(Facility facility);

    List<Facility> getAllFacilities();
}