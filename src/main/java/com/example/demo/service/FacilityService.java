package com.example.demo.service;

import com.example.demo.model.*;
import java.util.List;

public interface FacilityService {
    Facility addFacility(Facility f);
    List<Facility> getAllFacilities();
}
