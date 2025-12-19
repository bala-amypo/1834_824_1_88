package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.User;

public interface UserService {

    User saveUser(User user);

    Optional<User> findByEmail(String email);

    List<User> getAllUsers();
}