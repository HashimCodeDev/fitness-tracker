package com.example.fitnesstracker.repository;

import com.example.fitnesstracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> { }
