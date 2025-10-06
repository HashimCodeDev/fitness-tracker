package com.example.fitnesstracker.controller;

import com.example.fitnesstracker.dto.WorkoutRequest;
import com.example.fitnesstracker.model.Exercise;
import com.example.fitnesstracker.model.User;
import com.example.fitnesstracker.model.Workout;
import com.example.fitnesstracker.repository.UserRepository;
import com.example.fitnesstracker.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    @Autowired
    private WorkoutRepository workoutRepository;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Workout> createWorkout(@RequestBody WorkoutRequest request, @RequestParam String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.badRequest().build();
        
        Workout workout = new Workout();
        workout.setTitle(request.getTitle());
        workout.setDate(LocalDate.now());
        workout.setUser(user);
        
        List<Exercise> exercises = request.getExercises().stream().map(er -> {
            Exercise exercise = new Exercise();
            exercise.setName(er.getName());
            exercise.setSets(er.getSets());
            exercise.setReps(er.getReps());
            exercise.setWorkout(workout);
            return exercise;
        }).collect(Collectors.toList());
        
        workout.setExercises(exercises);
        return ResponseEntity.ok(workoutRepository.save(workout));
    }

    @GetMapping
    public ResponseEntity<List<Workout>> getUserWorkouts(@RequestParam String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(workoutRepository.findByUserOrderByDateDesc(user));
    }
}