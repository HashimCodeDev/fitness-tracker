package com.example.fitnesstracker.service;

import com.example.fitnesstracker.model.User;
import com.example.fitnesstracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAllUsers() { return repo.findAll(); }
    public User saveUser(User user) { return repo.save(user); }
    public void deleteUser(Long id) { repo.deleteById(id); }
}
