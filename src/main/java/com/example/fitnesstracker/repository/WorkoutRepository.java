package com.example.fitnesstracker.repository;

import com.example.fitnesstracker.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout, Long> { }
