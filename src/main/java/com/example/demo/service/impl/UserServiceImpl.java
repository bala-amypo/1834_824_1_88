package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository r, PasswordEncoder p){
        repo=r; encoder=p;
    }

    public User register(User u){
        if(repo.existsByEmail(u.getEmail()))
            throw new BadRequestException("Email exists");
        u.setPassword(encoder.encode(u.getPassword()));
        return repo.save(u);
    }
}
