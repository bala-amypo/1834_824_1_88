package com.example.demo.service;

import com.example.demo.model.User;

import java.util.List;

public interface UserService {

    // ✅ REQUIRED BY TEST & AUTH CONTROLLER
    User register(User user);

    // ✅ REQUIRED BY TEST
    User findByEmail(String email);

    // ✅ REQUIRED BY TEST
    List<User> getAllUsers();

    // ✅ REQUIRED BY TEST
    User saveUser(User user);
}
