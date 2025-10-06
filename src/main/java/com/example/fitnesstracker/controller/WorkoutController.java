package com.example.fitnesstracker.controller;

import com.example.fitnesstracker.dto.WorkoutRequest;
import com.example.fitnesstracker.model.Exercise;
import com.example.fitnesstracker.model.User;
import com.example.fitnesstracker.model.Workout;
import com.example.fitnesstracker.repository.UserRepository;
import com.example.fitnesstracker.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    @GetMapping("/history")
    public ResponseEntity<List<Workout>> getWorkoutHistory(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        User user = findUserByEmailOrId(email, userId);
        if (user == null) return ResponseEntity.badRequest().build();
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Workout> workouts = workoutRepository.findByUser(user, pageable);
        return ResponseEntity.ok(workouts.getContent());
    }

    @GetMapping("/history/date-range")
    public ResponseEntity<List<Workout>> getWorkoutHistoryByDateRange(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        
        User user = findUserByEmailOrId(email, userId);
        if (user == null) return ResponseEntity.badRequest().build();
        
        List<Workout> workouts = workoutRepository.findByUserAndDateBetweenOrderByDateDesc(user, startDate, endDate);
        return ResponseEntity.ok(workouts);
    }

    @GetMapping("/history/{workoutId}")
    public ResponseEntity<Workout> getWorkoutById(
            @PathVariable Long workoutId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long userId) {
        
        User user = findUserByEmailOrId(email, userId);
        if (user == null) return ResponseEntity.badRequest().build();
        
        Optional<Workout> workout = workoutRepository.findByIdAndUser(workoutId, user);
        return workout.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/history/stats")
    public ResponseEntity<WorkoutStats> getWorkoutStats(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long userId) {
        
        User user = findUserByEmailOrId(email, userId);
        if (user == null) return ResponseEntity.badRequest().build();
        
        List<Workout> allWorkouts = workoutRepository.findByUserOrderByDateDesc(user);
        
        WorkoutStats stats = new WorkoutStats();
        stats.setTotalWorkouts(allWorkouts.size());
        stats.setTotalExercises(allWorkouts.stream().mapToInt(w -> w.getExercises().size()).sum());
        
        if (!allWorkouts.isEmpty()) {
            stats.setLastWorkoutDate(allWorkouts.get(0).getDate());
            stats.setFirstWorkoutDate(allWorkouts.get(allWorkouts.size() - 1).getDate());
        }
        
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/history/{workoutId}")
    public ResponseEntity<Void> deleteWorkout(
            @PathVariable Long workoutId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long userId) {
        
        User user = findUserByEmailOrId(email, userId);
        if (user == null) return ResponseEntity.badRequest().build();
        
        Optional<Workout> workout = workoutRepository.findByIdAndUser(workoutId, user);
        if (workout.isPresent()) {
            workoutRepository.delete(workout.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private User findUserByEmailOrId(String email, Long userId) {
        if (email != null) {
            return userRepository.findByEmail(email).orElse(null);
        } else if (userId != null) {
            return userRepository.findById(userId).orElse(null);
        }
        return null;
    }

    public static class WorkoutStats {
        private int totalWorkouts;
        private int totalExercises;
        private LocalDate lastWorkoutDate;
        private LocalDate firstWorkoutDate;

        public int getTotalWorkouts() { return totalWorkouts; }
        public void setTotalWorkouts(int totalWorkouts) { this.totalWorkouts = totalWorkouts; }
        public int getTotalExercises() { return totalExercises; }
        public void setTotalExercises(int totalExercises) { this.totalExercises = totalExercises; }
        public LocalDate getLastWorkoutDate() { return lastWorkoutDate; }
        public void setLastWorkoutDate(LocalDate lastWorkoutDate) { this.lastWorkoutDate = lastWorkoutDate; }
        public LocalDate getFirstWorkoutDate() { return firstWorkoutDate; }
        public void setFirstWorkoutDate(LocalDate firstWorkoutDate) { this.firstWorkoutDate = firstWorkoutDate; }
    }
}