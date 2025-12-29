package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    // ✅ EXACT CONSTRUCTOR (TEST EXPECTED)
    public UserServiceImpl(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    // ✅ REGISTER USER
    @Override
    public User register(User user) {
        if (repo.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    // ✅ REQUIRED BY AUTH CONTROLLER & TEST
    @Override
    public User findByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    // ✅ REQUIRED BY TEST
    @Override
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    // ✅ REQUIRED BY TEST
    @Override
    public User saveUser(User user) {
        return repo.save(user);
    }
}
