package com.example.fitnesstracker.service;

import com.example.fitnesstracker.model.Workout;
import com.example.fitnesstracker.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutService {
    private final WorkoutRepository repo;

    public WorkoutService(WorkoutRepository repo) {
        this.repo = repo;
    }

    public List<Workout> getAllWorkouts() { return repo.findAll(); }
    public Workout saveWorkout(Workout workout) { return repo.save(workout); }
    public void deleteWorkout(Long id) { repo.deleteById(id); }
}
