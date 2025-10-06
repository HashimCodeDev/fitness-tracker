package com.example.fitnesstracker.repository;

import com.example.fitnesstracker.model.Workout;
import com.example.fitnesstracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserOrderByDateDesc(User user);
}